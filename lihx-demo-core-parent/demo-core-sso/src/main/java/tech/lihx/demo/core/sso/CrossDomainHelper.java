package tech.lihx.demo.core.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.common.CookieHelper;
import tech.lihx.demo.core.sso.common.encrypt.AES;
import tech.lihx.demo.core.sso.common.util.ReflectUtil;

import com.alibaba.fastjson.JSON;

/**
 * 跨域帮助类
 * <p>
 * 1、业务系统访问 SSO 保护系统、 验证未登录跳转至 SSO系统。 2、SSO 设置信任Cookie 生成询问密文，通过代理页面 JSONP
 * 询问业务系统是否允许登录。 2、业务系统验证询问密文生成回复密文。 3、代理页面 getJSON SSO 验证回复密文，SSO 根据 ok 返回
 * userId 查询绑定关系进行登录，通知代理页面重定向到访问页面。
 * 
 * @author LHX
 * @Date 2016-06-27
 */
public class CrossDomainHelper {

	private final static Logger logger = LoggerFactory.getLogger(CrossDomainHelper.class);


	/**
	 * 设置跨域信任 Cookie
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 */
	public static void setAuthCookie( HttpServletRequest request, HttpServletResponse response, AuthToken authToken ) {
		try {
			CookieHelper.addCookie(
				response, SSOConfig.getCookieDomain(), SSOConfig.getCookiePath(), SSOConfig.getAuthCookieName(),
				CoreHelper.encryptCookie(request, authToken, ReflectUtil.getConfigEncrypt()),
				SSOConfig.getAuthCookieMaxage(), true, SSOConfig.getCookieSecure());
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			logger.error("AuthToken encryptCookie error.");
		}
	}


	/**
	 * 获取跨域信任 AuthToken
	 * 
	 * @param request
	 * @param publicKey
	 *            RSA 公钥 (SSO)
	 * @return
	 */
	public static AuthToken getAuthToken( HttpServletRequest request, String publicKey ) {
		String jsonToken = CoreHelper.getJsonToken(
			request, ReflectUtil.getConfigEncrypt(), SSOConfig.getAuthCookieName());
		if ( jsonToken == null || "".equals(jsonToken) ) {
			logger.info("jsonToken is null.");
			return null;
		} else {
			/**
			 * 校验 IP 合法返回 AuthToken
			 */
			AuthToken at = JSON.parseObject(jsonToken, AuthToken.class);
			if ( CoreHelper.checkIp(request, at) == null ) { return null; }
			return at.verify(publicKey);
		}
	}


	/**
	 * 生成跨域询问密文
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 * @param aesKey
	 *            AES 密钥
	 */
	public static String askCiphertext( AuthToken authToken, String aesKey ) {
		try {
			return new AES().encrypt(authToken.jsonToken(), aesKey);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			logger.info("askCiphertext AES encrypt error.");
		}
		return null;
	}


	/**
	 * 生成跨域回复密文
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 * @param userId
	 *            用户ID
	 * @param askTxt
	 *            询问密文
	 * @param privateKey
	 *            RSA 密钥 (业务系统)
	 * @param publicKey
	 *            RSA 公钥 (SSO)
	 * @param aesKey
	 *            AES 密钥
	 */
	public static String
			replyCiphertext(
					HttpServletRequest request, String userId, String askTxt, String privateKey, String publicKey,
					String aesKey ) {
		String at = null;
		try {
			at = new AES().decrypt(askTxt, aesKey);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			logger.info("replyCiphertext AES decrypt error.");
		}
		if ( at != null ) {
			AuthToken authToken = JSON.parseObject(at, AuthToken.class);
			if ( CoreHelper.checkIp(request, authToken.verify(publicKey)) != null ) {
				authToken.setUserId(userId);
				try {
					/**
					 * 使用业务系统密钥 重新签名
					 */
					authToken.sign(privateKey);
					return new AES().encrypt(authToken.jsonToken(), aesKey);
				} catch ( Exception e ) {
					logger.error(e.getMessage(), e);
					logger.info("replyCiphertext AES encrypt error.");
				}
			}
		}
		return null;
	}


	/**
	 * 验证回复密文，成功! 返回绑定用户ID
	 * 
	 * @param request
	 * @param response
	 * @param authToken
	 *            跨域信任 Token
	 * @param askTxt
	 *            询问密文
	 * @param tokenPk
	 *            RSA 公钥 (业务系统 AuthToken加密公钥)
	 * @param replyPk
	 *            RSA 公钥 (SSO 回复密文公钥)
	 * @param aesKey
	 *            AES 密钥
	 * @return 用户ID
	 */
	public static String ok(
			HttpServletRequest request, HttpServletResponse response, String replyTxt, String tokenPk, String replyPk,
			String aesKey ) {
		AuthToken authToken = getAuthToken(request, tokenPk);
		if ( authToken != null ) {
			String rt = null;
			try {
				rt = new AES().decrypt(replyTxt, aesKey);
			} catch ( Exception e ) {
				logger.error(e.getMessage(), e);
				logger.info("kisso AES decrypt error.");
			}
			if ( rt != null ) {
				AuthToken atk = JSON.parseObject(rt, AuthToken.class);
				if ( atk != null && atk.getUuid().equals(authToken.getUuid()) ) {
					if ( atk.verify(replyPk) != null ) {
						/**
						 * 删除跨域信任Cookie 返回 userId
						 */
						CookieHelper.clearCookieByName(
							request, response, SSOConfig.getAuthCookieName(), SSOConfig.getCookieDomain(),
							SSOConfig.getCookiePath());
						return atk.getUserId();
					}
				}
			}
		}
		return null;
	}

}
