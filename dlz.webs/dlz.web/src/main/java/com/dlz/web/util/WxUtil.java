package com.dlz.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import com.dlz.comm.consts.Charsets;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;

import com.dlz.app.uim.holder.ThirdHolder;
import com.dlz.app.uim.holder.ThirdHolder.ThirdInfo;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.exception.RemoteException;
import com.dlz.comm.exception.SystemException;
import com.dlz.framework.holder.TokenHolder;
import com.dlz.framework.holder.TokenHolder.TokenInfo;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.encry.Base64;
import com.dlz.web.holder.ThreadHolder;


/**
 * 通用工具类
 * 
 * @author dk（微信）
 *
 */
public class WxUtil {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(WxUtil.class);

	// 凭证获取(GET)
	public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 凭证获取(GET)
	public final static String ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=TOKEN&type=jsapi";
	// 凭证获取(GET)
	public final static String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSEC&code=CODE&grant_type=authorization_code";
	// 凭证获取(GET)
	public final static String authorize_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=FULURL&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	// 凭证获取(GET)
	public final static String useriinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	// 凭证获取(GET)
	public final static String info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//二维码创建接口
	public final static String qrcode_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	
	//小程序code换取sessionkey接口
	public final static String sessionkey_url ="https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
	
	//小程序发送模板消息
	private static String xcx_message_model_url="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
	
	//公众号发送模板消息
	private static String message_model_url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	private final static String TOKEN_NAME = "token";
	private final static String TICKET_NAME = "ticket";
	
	private static String SETTING_HOST = null;//系统host
	public static void init(String appids,String localhost) {
		if(appids!=null){
			String[] apps=appids.split("\\|");
			for(String app:apps){
				String[] appinfos=app.split(",");
				WxConfig.init(appinfos[1],appinfos[2],appinfos[0]);
			}
		}
		SETTING_HOST = localhost;
	}
	
	/**
	 * token工具
	 * @author dingkui
	 *
	 */
	public static class AccessToken{
		/**
		 * 获取默认接口访问凭证
		 * 
		 * @return
		 */
		public static String getAccessToken() {
			return getAccessToken(WxConfig.getAppid(), WxConfig.getSecret(WxConfig.getAppid()));
		}
		public static String getAccessToken(String appId) {
			return getAccessToken(appId, WxConfig.getSecret(appId));
		}
		public static String getAccessToken(String appId, String appSecret) {
			synchronized (WxUtil.class) {
				TokenInfo wxToken = TokenHolder.getToken(TOKEN_NAME + appId);
				if (wxToken.isUsefull()) {
					return wxToken.getTokenStr(); // 返回有效的token
				}
				String requestUrl = token_url.replace("APPID", appId).replace("APPSECRET", appSecret);
				// 发起GET请求获取凭证
				JSONMap jsonObject = HttpUtil.HttpUtilEnum.GET.send4JSON(requestUrl);//httpsRequest(requestUrl, "GET", null);
				if (jsonObject != null) {
					try {
						wxToken.setExpiresIn(jsonObject.getInt("expires_in"), jsonObject.getStr("access_token"));
						if (wxToken.isUsefull()) {
							return wxToken.getTokenStr(); // 返回有效的token
						}
					} catch (Exception e) {
						wxToken.setExpiresIn(0, null);
						// 获取 token 失败
						logger.error("获取token失败 errcode:{0} errmsg:{1}", jsonObject.getInt("errcode"),
								jsonObject.getStr("errmsg"));
						logger.error(e.getMessage(), e);
					}
				}
				return null;
			}
		}
		/**
		 * 获取小程序默认接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static String getXcxAccessToken() {
			return getXcxAccessToken(WxConfig.getXcxAppid(), WxConfig.getSecret(WxConfig.getXcxAppid()));
		}
		public static String getXcxAccessToken(String appId) {
			return getXcxAccessToken(appId, WxConfig.getSecret(appId));
		}
		public static String getXcxAccessToken(String xcxAppId, String xcxAppSecret) {
			synchronized (WxUtil.class) {
				TokenInfo wxToken = TokenHolder.getToken(TOKEN_NAME + xcxAppId);
				if (wxToken.isUsefull()) {
					return wxToken.getTokenStr(); // 返回有效的token
				}
				String requestUrl = token_url.replace("APPID", xcxAppId).replace("APPSECRET", xcxAppSecret);
				// 发起GET请求获取凭证
				JSONMap jsonObject = HttpUtil.HttpUtilEnum.GET.send4JSON(requestUrl);//httpsRequest(requestUrl, "GET", null);
				if (jsonObject != null) {
					try {
						wxToken.setExpiresIn(jsonObject.getInt("expires_in"), jsonObject.getStr("access_token"));
						if (wxToken.isUsefull()) {
							return wxToken.getTokenStr(); // 返回有效的token
						}
					} catch (Exception e) {
						wxToken.setExpiresIn(0, null);
						// 获取 token 失败
						logger.error("获取token失败 errcode:{0} errmsg:{1}", jsonObject.getInt("errcode"),
								jsonObject.getStr("errmsg"));
						logger.error(e.getMessage(), e);
					}
				}
				return null;
			}
		}
		
		@SuppressWarnings("unchecked")
		public static String getQrcodeUrl(String appid,String appsecret,String scene_str, Boolean isExprise){
			String token=getAccessToken(appid, appsecret);
			String requestUrl = qrcode_url.replace("TOKEN", token);
			JSONMap obj = new JSONMap();
			JSONMap info = new JSONMap();
			JSONMap scene = new JSONMap();
			scene.put("scene_str",scene_str);
			info.put("scene", scene);
			if(isExprise){
				obj.put("expire_seconds", 2592000);//有效期一个月
				obj.put("action_name", "QR_STR_SCENE");
			}else{
				obj.put("action_name", "QR_LIMIT_STR_SCENE");
			}
			obj.put("action_info", info);
			String result = httpsRequest(requestUrl, "POST", obj.toString());
			Map<String, Object> resultMap = JacksonUtil.readValue(result, Map.class);
			if(resultMap!=null){
				return resultMap.get("url").toString();
			}
			return null;
		}
		public static String getQrcodeUrl(String scene_str, Boolean isExprise){
			return getQrcodeUrl(WxConfig.getAppid(), WxConfig.getSecret(WxConfig.getAppid()), scene_str, isExprise);
		}
	}
	public static class AccessTicket{
		/**
		 * 获取接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static String getTicket() {
			return getTicket(WxConfig.getAppid(), WxConfig.getSecret(WxConfig.getAppid()));
		}
		/**
		 * 获取接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static String getTicket(String appId) {
			return getTicket(appId, WxConfig.getSecret(appId));
		}

		public static String getTicket(String appId, String appSecret) {
			synchronized (WxUtil.class) {
				TokenInfo wxTicket = TokenHolder.getToken(TICKET_NAME + appId);
				try {
					if (wxTicket.isUsefull()) {
						return wxTicket.getTokenStr(); // 返回有效的token
					}
		
					String accessToken = AccessToken.getAccessToken(appId, appSecret);
					if (accessToken == null) {
						return null; // 返回有效的token
					}
		
					String requestUrl = ticket_url.replace("TOKEN", accessToken);
					String ruselt = HttpUtil.HttpUtilEnum.GET.send(requestUrl);
					JSONMap resultMap = JacksonUtil.readValue(ruselt, JSONMap.class);
					if ("0".equals(resultMap.getStr("errcode"))) {// 成功
						wxTicket.setExpiresIn(resultMap.getInt("expires_in"), resultMap.getStr("ticket"));
						if (wxTicket.isUsefull()) {
							return wxTicket.getTokenStr(); // 返回有效的token
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				return null;
			}
		}
	}
	
	
	public static class WxConfig{
		private static Map<String,String> configMap = new HashMap<String,String>();//多个appID
		private static String DEFAULT_APPID = null;//初始化设置的appID
		private static String DEFAULT_XCX_APPID = null;//初始化设置小程序的appID
		static void add(String appid, String secret){
			configMap.put(appid, secret);
		}
		static String getSecret(String appid){
			String secret=configMap.get(appid);
			if(secret==null){
				throw new SystemException("微信参数未初始化：secret为空！");
			}
			return secret;
		}
		static void init(String appid, String secret,String type){
			switch(type){
			  case "1":
				  DEFAULT_APPID=appid;
				  break;
			  case "2":
				  DEFAULT_XCX_APPID=appid;
				  break;
			}
			add(appid, secret);
		}
		public static String getAppid(){
			if(DEFAULT_APPID==null){
				throw new SystemException("微信参数未初始化：appid为空！");
			}
			return DEFAULT_APPID;
		}
		public static String getXcxAppid(){
			if(DEFAULT_XCX_APPID==null){
				throw new SystemException("微信参数未初始化：小程序appid为空！");
			}
			return DEFAULT_XCX_APPID;
		}
		// 生成签名
		public static Map<String, String> createConfigJson(String current_url) {
			return createConfigJson(current_url, getAppid(), getSecret(getAppid()));
		}
		public static Map<String, String> createConfigJson(String current_url, String appId) {
			if(StringUtils.isEmpty(appId)){
				appId=getAppid();
			}
			return createConfigJson(current_url, appId, getSecret(appId));
		}
		public static Map<String, String> createConfigJson(String current_url, String appId, String appSecret) {
			String jsapi_ticket = AccessTicket.getTicket(appId, appSecret);
			Map<String, String> params = new HashMap<String, String>();
			params = sign(jsapi_ticket, current_url);
			params.put("appid", appId);
			return params;
		}
		
		private static String byteToHex(final byte[] hash) {
			Formatter formatter = new Formatter();
			for (byte b : hash) {
				formatter.format("%02x", b);
			}
			String result = formatter.toString();
			formatter.close();
			return result;
		}
	
		private static String create_nonce_str() {
			return UUID.randomUUID().toString();
		}
	
		private static String create_timestamp() {
			return Long.toString(System.currentTimeMillis() / 1000);
		}
	
		
		public static Map<String, String> sign(String jsapi_ticket, String url) {
			Map<String, String> ret = new HashMap<String, String>();
			String nonce_str = create_nonce_str();
			String timestamp = create_timestamp();
			String string1;
			String signature = "";
	
			// 注意这里参数名必须全部小写，且必须有序
			string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
	
			try {
				MessageDigest crypt = MessageDigest.getInstance("SHA-1");
				crypt.reset();
				crypt.update(string1.getBytes("UTF-8"));
				signature = byteToHex(crypt.digest());
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.getMessage(),e);
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(),e);
			}
			ret.put("url", url);
			ret.put("jsapi_ticket", jsapi_ticket);
			ret.put("nonceStr", nonce_str);
			ret.put("timestamp", timestamp);
			ret.put("signature", signature);
			return ret;
		}
	}

	public static class UserInfo{
		/**
		 * 获取用户信息（用户页面授权）
		 * 获取用户接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static ThirdInfo getThirdInfoByCode(String code,String appid,String secret) {
			return getThirdInfoByCode(code, appid, secret, true);
		}
		
		/**
		 * 获取用户信息（用户页面授权）
		 * 获取用户接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static ThirdInfo getThirdInfoByCode(String code,String appid,String secret,boolean needUserInfo) {
			TokenInfo token = ThirdHolder.getWxMemberAccesToken();
			ThirdInfo thirdInfo=ThirdHolder.getThirdInfo();
			if (thirdInfo.isFromWx() || token.isUsefull()) {
				return thirdInfo;
			}
			if (code != null) {
				String requestUrl = access_token_url.replace("APPID", appid).replace("APPSEC", secret).replace("CODE", code);
				String result=null;
				try {
					result = HttpUtil.HttpUtilEnum.GET.send(requestUrl);
				} catch (Exception e) {
					throw RemoteException.buildException("取得信息失败："+e.getMessage(), e);
				}
				JSONMap userInfo = JSONMap.createJsonMap(result);
				if (userInfo.containsKey("errcode")) { // 是否有错误
					if (userInfo.getLong("errcode")==40163) { // 是否有错误
						logger.warn("Code失效，重新取得：code={1}，{0}",code, result);
						return null;
					}
					logger.warn("用户openid取得失败：{0}", result);
					return null;
				}
				token.setExpiresIn(userInfo.getInt("expires_in"), userInfo.getStr("access_token"));
				
				thirdInfo.setWx_openid(userInfo.getStr("openid"));
				//如果unionid没有取到则用openid作为unionid
				if(userInfo.containsKey("unionid")){
					thirdInfo.setWx_unionid(userInfo.getStr("unionid"));
				}else{
					thirdInfo.setWx_unionid(userInfo.getStr("openid"));
				}
				
				if(needUserInfo){
					UserInfo.getThirdInfoByToken(userInfo.getStr("access_token"),userInfo.getStr("openid"));
				}
				return thirdInfo;
			}
			return thirdInfo;
		}
		
		/**
		 * 获取用户信息（用户页面授权）
		 * 获取用户接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static ThirdInfo getThirdInfoByCode(String code) {
			return getThirdInfoByCode(code,true);
		}
		
		/**
		 * 获取用户信息（用户页面授权）
		 * 获取用户接口访问凭证
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static ThirdInfo getThirdInfoByCode(String code,boolean needuserInfo) {
			return getThirdInfoByCode(code, WxConfig.getAppid(), WxConfig.getSecret(WxConfig.getAppid()),needuserInfo);
		}
		/**
		 * 获取用户信息（用户页面授权）
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static ThirdInfo getThirdInfoByToken(String accessToken,String openid) {
			ThirdInfo thirdInfo=ThirdHolder.getThirdInfo();
			if (thirdInfo.isFromWx() ) {
				return thirdInfo;
			}
			if (accessToken == null) {
				return null;
			}
			try {
				String requestUrl = useriinfo_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
				String resUserInfo = HttpUtil.HttpUtilEnum.GET.send(requestUrl);
				logger.debug("----Get the user basic information:user OpenId:{0},Returns the result：{1}", openid,resUserInfo);
				// {"subscribe":1,"openid":"ocdGC0d6-3DOutKcuJkdB9_H0f_g","nickname":"锤哥","sex":1,"language":"zh_CN","city":"武汉","province":"湖北","country":"中国",
				// "headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/rpJnNOZphx5t2x24RKCSr5bUqaicC7SVicibrpJnfzDnBkicOAb8v56esfEPgjdH7Dg2STqySic9GW3fsFVOu373WL39Qb7NK83eL\/0",
				// "subscribe_time":1491816542,"unionid":"oehLQwVChkSbanOXocB2bEtkGH44","remark":"","groupid":0,"tagid_list":[]}
				// {"subscribe":0,"openid":"ocdGC0Re7vimutGa4LqudxEZtuC4","unionid":"oehLQwW6OB7AGo_vB53S334o1_5U","tagid_list":[]}
				JSONMap userInfo = new JSONMap(resUserInfo);
				if(userInfo!=null){
					thirdInfo.setWx_openid(userInfo.getStr("openid"));
					//如果unionid没有取到则用openid作为unionid
					if(userInfo.containsKey("unionid")){
						thirdInfo.setWx_unionid(userInfo.getStr("unionid"));
					}else{
						thirdInfo.setWx_unionid(userInfo.getStr("openid"));
					}
					if(userInfo.containsKey("headimgurl")){
						thirdInfo.setFace(userInfo.getStr("headimgurl"));
					}
					if(userInfo.containsKey("nickname")){
						thirdInfo.setNickname(userInfo.getStr("nickname"));
					}
					if(userInfo.containsKey("sex")){
						thirdInfo.setSex(userInfo.getStr("sex"));
					}
					thirdInfo.setFromWx(true);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			return thirdInfo;
		}

		/**
		 *  获取用户信息（后台）
		 *  用户关注公众号则可以取到完整信息，没有关注，只能openid和unionid
		 * @param openid
		 * @return
		 */
		public static ThirdInfo getThirdInfoByOpenid(String openid) {
			ThirdInfo thirdInfo=ThirdHolder.getThirdInfo();
			if (thirdInfo.isFromWx() ) {
				return thirdInfo;
			}
			// 获取用户的微信信息
			try {
				if (openid == null) {
					return null;
				}
				String accessToken = AccessToken.getAccessToken();
				String requestUrl = info_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
				String resUserInfo = HttpUtil.HttpUtilEnum.GET.send(requestUrl);
				logger.debug("----Get the user basic information:user OpenId:{0},Returns the result：{1}", openid,resUserInfo);
				// {"subscribe":1,"openid":"ocdGC0d6-3DOutKcuJkdB9_H0f_g","nickname":"锤哥","sex":1,"language":"zh_CN","city":"武汉","province":"湖北","country":"中国",
				// "headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/rpJnNOZphx5t2x24RKCSr5bUqaicC7SVicibrpJnfzDnBkicOAb8v56esfEPgjdH7Dg2STqySic9GW3fsFVOu373WL39Qb7NK83eL\/0",
				// "subscribe_time":1491816542,"unionid":"oehLQwVChkSbanOXocB2bEtkGH44","remark":"","groupid":0,"tagid_list":[]}

				// {"subscribe":0,"openid":"ocdGC0Re7vimutGa4LqudxEZtuC4","unionid":"oehLQwW6OB7AGo_vB53S334o1_5U","tagid_list":[]}
				JSONMap userInfo = JacksonUtil.readValue(resUserInfo, JSONMap.class);
				if(userInfo!=null){
					thirdInfo.setWx_openid(userInfo.getStr("openid"));
					if(userInfo.containsKey("unionid")){
						thirdInfo.setWx_unionid(userInfo.getStr("unionid"));
					}else{
						thirdInfo.setWx_unionid(userInfo.getStr("openid"));
					}
					if(userInfo.containsKey("headimgurl")){
						thirdInfo.setFace(userInfo.getStr("headimgurl"));
					}
					if(userInfo.containsKey("nickname")){
						thirdInfo.setNickname(userInfo.getStr("nickname"));
					}
					if(userInfo.containsKey("sex")){
						thirdInfo.setSex(userInfo.getStr("sex"));
					}
					thirdInfo.setFromWx(true);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			return thirdInfo;
		}

		/**
		 * 取得用户授权地址
		 * @param response
		 * @param fulUrl
		 * @param scope
		 *            snsapi_userinfo or snsapi_base
		 * @throws IOException
		 */
		public static String getAuthorizeUrl(String fulUrl, String scope, HttpServletRequest request) throws IOException {
			if (fulUrl == null) {
				fulUrl = SETTING_HOST + request.getServletPath();
				String queryString = request.getQueryString();
				if (queryString != null && !"".equals(queryString)) {
					fulUrl += "?" + queryString;
				}
			}
			fulUrl = fulUrl.replaceAll("(&|\\?)code=.*$", "");
			if (fulUrl.indexOf("?") > -1) {
				fulUrl = URLEncoder.encode(fulUrl, "UTF-8");
			}
			logger.debug(fulUrl);
			if (scope == null) {
				// scope="snsapi_base";//取得用户openid不需要显示授权
				scope = "snsapi_userinfo";//取得用户基本信息，需要用户授权
			}
			return authorize_url.replace("APPID", WxConfig.getAppid()).replace("FULURL", fulUrl).replace("SCOPE", scope);
		}
		
		public static String getXcxSessionKey(String code) {
			String XcxAppId=WxConfig.getXcxAppid(); 
			String XcxAppSecret=WxConfig.getSecret(XcxAppId);
			String resUserInfo=null;
			if(!StringUtils.isEmpty(code)){
				String requestUrl = sessionkey_url.replace("APPID", XcxAppId).replace("SECRET", XcxAppSecret).replace("JSCODE", code);
				resUserInfo = HttpUtil.HttpUtilEnum.GET.send(requestUrl);
			}		
			return resUserInfo;
		}
		
		/**
		 * 获取用户信息（用户页面授权）
		 * 
		 * @param appid
		 * @param appsecret
		 * @return
		 */
		public static ThirdInfo getXcxThirdInfoByCode(String code,String encryptedData,String iv) {
			ThirdInfo thirdInfo=ThirdHolder.getThirdInfo();
			String XcxAppId=WxConfig.getXcxAppid(); 
			String XcxAppSecret=WxConfig.getSecret(XcxAppId);
			if (thirdInfo.isFromWx() ) {
				return thirdInfo;
			}
			if (code == null) {
				return null;
			}
			try {
				String requestUrl = sessionkey_url.replace("APPID", XcxAppId).replace("SECRET", XcxAppSecret).replace("JSCODE", code);
				JSONMap sessionData = HttpUtil.HttpUtilEnum.GET.send4JSON(requestUrl);
				String sessionKey=sessionData.getStr("session_key");
				if(sessionKey==null){
					logger.error("session_key取得失败："+sessionData);
					return thirdInfo;
				}
				//session_key存起来用于获取用户绑定手机号
				ThreadHolder.getHttpRequest().getSession().setAttribute("session_key", sessionKey);
				
				String dataStr=decrypt(sessionData.getStr("session_key"), encryptedData, iv);
				JSONMap userInfo=JacksonUtil.readValue(dataStr,JSONMap.class);
				thirdInfo.setWx_xcx_openid(userInfo.getStr("openId"));
				thirdInfo.setFace(userInfo.getStr("avatarUrl"));
				thirdInfo.setNickname(userInfo.getStr("nickName"));
				thirdInfo.setSex(userInfo.getStr("gender"));
				//如果unionid没有取到则用openid作为unionid
				if(userInfo.containsKey("unionId")){
					thirdInfo.setWx_unionid(userInfo.getStr("unionId"));
				}else{
					thirdInfo.setWx_unionid(userInfo.getStr("openId"));
				}
				thirdInfo.setFromWx(true);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			return thirdInfo;
		}
	}
	
	/**
     * 小程序发送模板消息
     * @param accesstoken
     * @param touser
     * @param obj
     * @throws WexinReqException
     */
	public static void xcxModelMessage(String accesstoken, String touser,JSONMap obj) {

		if (accesstoken != null) {
			String requestUrl = xcx_message_model_url.replace("ACCESS_TOKEN", accesstoken);
			try {
				HttpUtil.HttpUtilEnum.POST.send(requestUrl, new StringEntity(obj.toString()));
				//System.out.println("微信返回的结果：" + result.toString());
			} catch (Exception e) {
				logger.debug("小程序发送模板消息出错：",e.getMessage());
			}
		} else {
			logger.debug("小程序发送模板消息出错");
		}
	}
	
    /**
     * 公众号发送模板消息
     * @param accesstoken
     * @param touser
     * @param obj
     * @throws WexinReqException
     */
	public static void modelMessage(String accesstoken, String touser,JSONMap obj){

		if (accesstoken != null) {
			String requestUrl = message_model_url.replace("ACCESS_TOKEN", accesstoken);
			try {
				HttpUtil.HttpUtilEnum.POST.send(requestUrl, new StringEntity(obj.toString()));
				//System.out.println("微信返回的结果：" + result.toString());
			} catch (Exception e) {
				logger.debug("公众号发送模板消息出错：",e.getMessage());
			}
		} else {
			logger.debug("公众号发送模板消息出错");
		}
	}
	
	/**
	 * 请求验证工具类
	 * 
	 * @author 陈孙亮（微信）
	 * @date 2014-06-25  
	 *
	 */
	public static class SignUtil {
		// 与接口配置信息中的Token要一致
		private static String token = "weixinCourse";
		
		/**
		 * 验证签名
		 * 
		 * @param signature
		 * @param timestamp
		 * @param nonce
		 * @return
		 */
		public static boolean checkSignature(String signature, String timestamp, String nonce){
			String[] arr = new String[] { token, timestamp, nonce };
			// 将token、timestamp、nonce三个参数进行字典序排序
			Arrays.sort(arr);
			StringBuilder content = new StringBuilder();
			
			for (int i = 0; i < arr.length; i++) {
				content.append(arr[i]);
			}
			MessageDigest md = null;
			String tmpStr = null;
			
			try{
				md = MessageDigest.getInstance("SHA-1");
				// 将三个参数字符串拼接成一个字符串进行sha1加密
				byte[] digest = md.digest(content.toString().getBytes());
				tmpStr = byteToStr(digest);
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			
			content = null;
			// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信 
			return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false; 
		}
		
		/**
		 * 将字节数组转换为十六进制字符串
		 * 
		 * @param byteArray
		 * @return
		 */
		private static String byteToStr(byte[] byteArray){
			String strDigest = ""; 
			for(int i = 0;i < byteArray.length; i++){
				strDigest += byteToHexStr(byteArray[i]);
			}
			return strDigest;
		}
		
		
		/**
		 * 将字节转换为十六进制字符串
		 * 
		 * @param mByte
		 * @return
		 */
		private static String byteToHexStr(byte mByte){
			char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			char[] tempArr = new char[2];  
			tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
			tempArr[1] = Digit[mByte & 0X0F];
			return new String(tempArr);
		}
	}
	
	/**
	 * 发送 https 请求
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	public static String httpsRequest(String requestUrl, String requestMethod,String outputStr){
		String tic=null;		
		try{
			//创建SSLContext 对象，并使用我们制定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
				}
			}};
			SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
			//从上述 SSLContext 对象中得到 SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setSSLSocketFactory(ssf);
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			
			//设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			
			//当outputStr 部位Null 时，想输入流写数据
			if(null != outputStr){
				OutputStream outputStream = conn.getOutputStream();
				//注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			
			//从输入六读取放回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader); 
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while((str = bufferedReader.readLine()) != null ){
				buffer.append(str);
			}
			
			//释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			tic=buffer.toString();
			
		}catch(Exception e){
		  e.printStackTrace();
		}
		
		return tic;
	}
	
	  /**
	   * AES解密
	   *
	   * @param encryptedData 消息密文
	   * @param ivStr         iv字符串
	   */
	  public static String decrypt(String sessionKey, String encryptedData, String ivStr) {
	    try {
	      AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
	      params.init(new IvParameterSpec(Base64.decode(ivStr.toCharArray())));

	      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decode(sessionKey.toCharArray()), "AES"), params);

	      return new String(PKCS7Encoder.decode(cipher.doFinal(Base64.decode(encryptedData.toCharArray()))), Charsets.UTF_8);
	    } catch (Exception e) {
	      throw new RuntimeException("AES解密失败", e);
	    }
	  }
	
}
