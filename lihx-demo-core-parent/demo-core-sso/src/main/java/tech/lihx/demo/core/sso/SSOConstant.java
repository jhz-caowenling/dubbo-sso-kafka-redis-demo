package tech.lihx.demo.core.sso;

/**
 * SSO 常量定义
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-9
 */
public class SSOConstant {

	/**
	 * 基本常量定义
	 */
	public final static String ENCODING = "UTF-8";

	public final static String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public final static String SSO_SECRET_KEY = "h2wmABdfM7i3K80mAS";

	public final static String CUT_SYMBOL = "#";

	/**
	 * Cookie 设置常量 maxage 介绍： -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 */
	public final static boolean SSO_COOKIE_SECURE = false;

	public final static boolean SSO_COOKIE_HTTPONLY = true;

	public final static int SSO_COOKIE_MAXAGE = -1;

	public final static String SSO_COOKIE_NAME = "uid";

	public final static String SSO_COOKIE_DOMAIN = "";

	public final static String SSO_COOKIE_PATH = "/";

	/**
	 * SSO 登录 Cookie 校验常量
	 */
	public final static boolean SSO_COOKIE_BROWSER = true;

	public final static boolean SSO_COOKIE_CHECKIP = false;

	public final static boolean SSO_COOKIE_CACHE = false;

	/**
	 * 自定义Encrypt Class
	 */
	public final static String SSO_ENCRYPT_CLASS = "";

	/**
	 * 自定义Token Class
	 */
	public final static String SSO_TOKEN_CLASS = "";

	/**
	 * 自定义TokenCache Class
	 */
	public final static String SSO_TOKENCACHE_CLASS = "";

	/**
	 * 登录相关常量
	 */
	public final static String SSO_LOGIN_URL = "http://sso.jiajiao.com/login.html";

	public final static String SSO_LOGOUT_URL = "http://sso.jiajiao.com/logout.html";

	/**
	 * 登录超时时间（秒） 默认 1800秒/30分钟
	 */
	public final static int SSO_LOGIN_LOSETIME = 1800;

	/**
	 * SSO 跨域相关常量 maxage 介绍： -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 */
	public final static String SSO_AUTH_COOKIE_NAME = "pid";

	public final static int SSO_AUTH_COOKIE_MAXAGE = 180;

	/**
	 * 拦截器判断后设置 Token至当前请求 request.setAttribute("ssotoken_attr", token)
	 */
	public final static String SSO_TOKEN_ATTR = "ssotoken_attr";
}
