package com.itbee.github.config;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * 微信菜单设置
 * @author itbee
 * @date 2017年11月4日 上午9:27:06
 *
 */
@Component
public class MenuConfig {



    public void createMenu(){
    
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext*.xml");
		    Map map=  (Map) context.getBean("wxProperties");
		    String domain =(String) map.get("domain");
			WxMpService wxMpService =(WxMpService) context.getBean("wxMpService");
   		    WxMenu menu = new WxMenu();
			WxMenuButton button1 = new WxMenuButton();
			button1.setType(WxConsts.MenuButtonType.VIEW);
			button1.setName("测试");
			
			String url =domain+"/weixindemo";
			button1.setUrl(url);
			menu.getButtons().add(button1);
			wxMpService.getMenuService().menuDelete();
			wxMpService.getMenuService().menuCreate(menu);
	    }catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	/**
	 * 运行此main函数即可生成公众号自定义菜单
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		MenuConfig menuConfig = new MenuConfig();
		menuConfig.createMenu();
		
		
	}

}
