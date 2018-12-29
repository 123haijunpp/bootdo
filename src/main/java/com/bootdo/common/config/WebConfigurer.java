package com.bootdo.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
class WebConfigurer extends WebMvcConfigurerAdapter {
	@Autowired
	BootdoConfig bootdoConfig;
	@Value("${uploadFilePath}")
	private String uploadFilePath;
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/files/**").addResourceLocations("file:C:/xd-REST/xd-REST/data/photo/**/");
		int year=2017;
		for (int i=0;i<20;i++){
			year=year+1;
			for (int j=1;j<=12;j++){
				registry.addResourceHandler("/files/"+year+"0"+j+"/**").addResourceLocations("file:C:/xd-REST/xd-REST/data/photo/"+year+"0"+j+"/");
			}
		}
	}

}