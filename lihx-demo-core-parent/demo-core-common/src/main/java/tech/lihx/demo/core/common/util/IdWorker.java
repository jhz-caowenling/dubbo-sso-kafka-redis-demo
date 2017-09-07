/**
 * IdWorker.java cn.vko.business.data Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.common.util;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 在分布式系统中，需要生成全局UID的场合还是比较多的，twitter的snowflake解决了这种需求，
 * 实现也还是很简单的，除去配置信息，核心代码就是毫秒级时间41位+机器ID 10位+毫秒内序列12位。
 * 该项目地址为：https://github.com/twitter/snowflake是用Scala实现的。
 * python版详见开源项目https://github.com/erans/pysnowflake。
 *
 * @author lihx
 * @Date 2017-9-5
 */
public class IdWorker {

	// 根据具体机器环境提供
	private final long workerId;

	// 滤波器,使时间变小,生成的总位数变小,一旦确定不能变动
	private final static long twepoch = 1361753741828L;

	private long sequence = 0L;

	private final static long workerIdBits = 10L;

	private final static long maxWorkerId = -1L ^ -1L << workerIdBits;

	private final static long sequenceBits = 12L;

	private final static long workerIdShift = sequenceBits;

	private final static long timestampLeftShift = sequenceBits + workerIdBits;

	private final static long sequenceMask = -1L ^ -1L << sequenceBits;

	private long lastTimestamp = -1L;

	// 根据主机id获取机器码
	private static IdWorker worker = new IdWorker();

	private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);

	// 主机和进程的机器码
	private static final int _genmachine;
	static {
		try {
			// build a 2-byte machine piece based on NICs info
			int machinePiece;
			{
				try {
					StringBuilder sb = new StringBuilder();
					Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
					while ( e.hasMoreElements() ) {
						NetworkInterface ni = e.nextElement();
						sb.append(ni.toString());
					}
					machinePiece = sb.toString().hashCode() << 16;
				} catch ( Throwable e ) {
					// exception sometimes happens with IBM JVM, use random
					logger.warn(e.getMessage(), e);
					machinePiece = new Random().nextInt() << 16;
				}
				logger.debug("machine piece post: " + Integer.toHexString(machinePiece));
			}

			// add a 2 byte process piece. It must represent not only the JVM
			// but the class loader.
			// Since static var belong to class loader there could be collisions
			// otherwise
			final int processPiece;
			{
				int processId = new java.util.Random().nextInt();
				try {
					processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
				} catch ( Throwable t ) {}

				ClassLoader loader = ObjectId.class.getClassLoader();
				int loaderId = loader != null ? System.identityHashCode(loader) : 0;

				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toHexString(processId));
				sb.append(Integer.toHexString(loaderId));
				processPiece = sb.toString().hashCode() & 0xFFFF;
				logger.debug("process piece: " + Integer.toHexString(processPiece));
			}

			_genmachine = machinePiece | processPiece;
			logger.debug("machine : " + Integer.toHexString(_genmachine));
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}

	}


	/**
	 * 创建 IdWorker对象.
	 *
	 * @Deprecated 请调用静态方法getId()
	 * @param workerId
	 */
	@Deprecated
	public IdWorker( final long workerId ) {
		if ( workerId > IdWorker.maxWorkerId || workerId < 0 ) { throw new IllegalArgumentException(String.format(
			"worker Id can't be greater than %d or less than 0", IdWorker.maxWorkerId)); }
		this.workerId = workerId;
	}


	public IdWorker() {
		// this.workerId = getAddress() % (IdWorker.maxWorkerId + 1);
		workerId = _genmachine % (IdWorker.maxWorkerId + 1);
	}


	public static long getId() {
		return worker.nextId();
	}


	public synchronized long nextId() {
		long timestamp = timeGen();
		if ( lastTimestamp == timestamp ) {
			sequence = sequence + 1 & IdWorker.sequenceMask;
			if ( sequence == 0 ) {
				// System.out.println("###########" + sequenceMask);//等待下一毫秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		if ( timestamp < lastTimestamp ) {
			try {
				throw new Exception(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
			} catch ( Exception e ) {
				logger.error(e.getMessage(), e);
			}
		}

		lastTimestamp = timestamp;
		long nextId = timestamp - twepoch << timestampLeftShift | workerId << IdWorker.workerIdShift | sequence;
		// System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
		// + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
		// + workerId + ",sequence:" + sequence);
		return nextId;
	}


	private long tilNextMillis( final long lastTimestamp1 ) {
		long timestamp = timeGen();
		while ( timestamp <= lastTimestamp1 ) {
			timestamp = timeGen();
		}
		return timestamp;
	}


	// private static long getAddress() {
	// try {
	// String currentIpAddress = InetAddress.getLocalHost().getHostAddress();
	// String[] str = currentIpAddress.split("\\.");
	// StringBuilder hardware = new StringBuilder();
	// for ( int i = 0 ; i < str.length ; i++ ) {
	// hardware.append(str[i]);
	// }
	// return Long.parseLong(hardware.toString());
	// } catch ( Exception e ) {
	// logger.error(e.getMessage(),e);
	// }
	//
	// return 2L;
	// }

	private long timeGen() {
		return System.currentTimeMillis();
	}


	public static void main( final String[] args ) {
		// IdWorker worker2 = new IdWorker(0);
		// System.out.println(worker2.nextId());
		// long ll = getAddress() % 16;
		// System.out.println(ll);
		// long start = System.currentTimeMillis();
		// for ( int i = 0 ; i < 100000 ; i++ ) {
		// getId();
		// }
		// long end = System.currentTimeMillis();
		// System.out.println((100000 / (end - start)) + "个/ms");
		// IdWorker worker2 = new IdWorker(2);
		System.out.println(getId());
		// 830762065
		// 830796545
		// System.out.println(_genmachine);
	}

}
