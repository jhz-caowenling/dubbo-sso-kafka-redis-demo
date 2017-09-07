package tech.lihx.demo.core.mybatis.mybatis;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import tech.lihx.demo.core.common.mybatis.Identity;
import tech.lihx.demo.core.common.util.IdWorker;
import tech.lihx.demo.core.mybatis.mybatis.ext.MapperProxyExt;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInvocation;

/**
 * 
 * 主键自动赋值实现
 */
@Intercepts( { @Signature( type = Executor.class, method = "update", args = { MappedStatement.class, Object.class } ) } )
public class IdentityInterceptor implements Interceptor {

	@Override
	public Object intercept( Invocation invocation ) throws Throwable {
		MyBatisInvocation inv = MapperProxyExt.getMyBatisInvocation();
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		if ( mappedStatement.getSqlCommandType() != SqlCommandType.INSERT ) { return invocation.proceed(); }
		// Object[] objs1 = invocation.getArgs();
		Object[] objs = inv.getArgs();

		if ( objs != null ) {
			for ( int i = 0 ; i < objs.length ; i++ ) {
				if ( objs[i] instanceof Identity ) {
					Identity identity = (Identity) objs[i];
					if ( identity.getId() != null ) {
						continue;
					}
					identity.setId(IdWorker.getId());
				} else if ( objs[i] instanceof List ) {
					List<?> list = (List<?>) objs[i];
					for ( Object object : list ) {
						if ( object instanceof Identity ) {
							Identity identity = (Identity) object;
							if ( identity.getId() != null ) {
								continue;
							}
							identity.setId(IdWorker.getId());
						}
					}
				}
			}
		}
		return invocation.proceed();

	}


	@Override
	public Object plugin( Object target ) {

		return Plugin.wrap(target, this);

	}


	@Override
	public void setProperties( Properties properties ) {

	}

}
