package com.itbee.github.contrlloer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

/**
 * 
 * @author itbee
 * @date 2017年11月4日 上午10:11:23
 *
 */
@Controller
public class WxController  {
	
    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService wxMpService;
   
    @Value("#{wxProperties.domain}")
	private String domain;
  

   
    
   

    /**
     * 微信公众号webservice主服务接口，提供与微信服务器的信息交互
     *公众号服务器配置的设置方法
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "core")
    public void core(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
      
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
       
        if (!this.wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echoStr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            String echoStrOut = String.copyValueOf(echoStr.toCharArray());
            response.getWriter().println(echoStrOut);
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;
    }
    /**
     * 跳转到微信网页授权页面
     * @author itbee
     * @date 2017年11月6日 下午2:02:47
     *
     */
    @RequestMapping(value ="auth")
    public String auth(HttpServletRequest request) {
    	
    	System.out.println("执行auth-------------");
    	
        String url = domain  +request.getContextPath()+"/weixincallback";
        System.out.println("url=="+url);
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO,"");
       
        return "redirect:" + redirectUrl;
    }
    /**
     * 打开手机在微信页面点击确定回调方法
     * @author itbee
     * @date 2017年11月6日 下午1:48:35
     *
     */
    @RequestMapping(value ="weixincallback")
    public String weixincallback(HttpServletRequest request,@RequestParam("code") String code,
                         @RequestParam("state") String state ,HttpServletResponse response) {
    	String returnUrl=domain  +request.getContextPath()+"/authsuccess";
    	System.out.println("执行weixincallback-------------");
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
       
        try {
        	
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            String openId = wxMpOAuth2AccessToken.getOpenId();
            System.out.println("openId:"+openId);

        } catch (WxErrorException e) {
           e.printStackTrace();
        }

        return "redirect:"+ returnUrl;
    }
    /**
     * 首页
     * @author itbee
     * @date 2017年11月6日 下午1:45:26
     *
     */
    @GetMapping(value = "index")
    public String index(HttpServletRequest request){
    	
    	return "index";
    }
    /**
     * 首页
     * @author itbee
     * @date 2017年11月6日 下午16:45:26
     *
     */
    @GetMapping(value = "authsuccess")
    public String authsuccess(HttpServletRequest request){
    	
    	return "authsuccess";
    }

}
