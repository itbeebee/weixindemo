package com.itbee.github.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceOkHttpImpl;


/**
 * 微信配置
 * @author itbee
 * @date 2017年11月4日 上午9:27:20
 *
 */
@Configuration
public class MainConfiguration {
	@Value("#{wxProperties.appId}")
	private String appId;

	@Value("#{wxProperties.appSecret}")
	private String appSecret;

	@Value("#{wxProperties.token}")
	private String token;

	

	@Bean
	public WxMpConfigStorage wxMpConfigStorage() {
		WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
		configStorage.setAppId(this.appId);
		configStorage.setSecret(this.appSecret);
		configStorage.setToken(this.token);
	
		return configStorage;
	}

	@Bean
	public WxMpService wxMpService() {
		WxMpService wxMpService = new WxMpServiceOkHttpImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
		return wxMpService;
	}
}
