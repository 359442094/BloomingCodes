package cn.blooming.bep.crawler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bloosoming.web.apiresponse.SKApiResponseBodyWrapFactoryBean;

@Configuration
public class SKApiResponseBodyWrapFactoryConfiguration {

	@Value("${response.wrapper.adminUrlSkipPrefix}")
	private String adminUrlSkipPrefix;
	
    @Bean
    public SKApiResponseBodyWrapFactoryBean getResponseBodyWrap() {
        return new SKApiResponseBodyWrapFactoryBean(this.adminUrlSkipPrefix);
    }
    
}
