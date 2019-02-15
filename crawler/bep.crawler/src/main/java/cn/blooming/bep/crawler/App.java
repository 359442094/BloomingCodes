package cn.blooming.bep.crawler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import com.bloosoming.rpcproxy.annotation.ImportAPIScan;
import com.bloosoming.rpcproxy.client.RPCRestTemplate;
import com.bloosoming.rpcproxy.server.AutoRpcRequestMappingHandlerMapping;

@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaClient
@EnableScheduling
@ComponentScan(basePackages = {
	    //"com.bloosoming.web.exceptionhandler",
	    "com.bloosoming.cat.exception",
	    "com.bloosoming.common",
	    "com.bloosoming.common.impl.cache",
		"com.bloosoming.cat.common",
		"com.bloosoming.cat.sql",
		"com.bloosoming.redis.config",
		"com.bloosoming.shutdown", // 正确清理关机时的controller线程
	})
@ComponentScan(basePackages = {
		"cn.blooming.bep.crawler.model.util",
		"cn.blooming.bep.crawler.data.controller",
		"com.bloosoming.rpcproxy.cache",
		"com.bloosoming.rpcproxy.client"
	})
@MapperScan({
		"cn.blooming.bep.crawler.model.mapper",
	 		 "com.bloosoming.common.eventlog"
	})
@Configuration
@ImportAPIScan({
		"cn.blooming.bep.baseservice.api",
		"cn.blooming.bep.fund.api",
		"cn.blooming.bep.data.api",
		"cn.blooming.bep.accounts.api"
	})
@Indexed
@EnableCaching
public class App
{
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

    @Bean
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
    	return new AutoRpcRequestMappingHandlerMapping();
    }
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RPCRestTemplate();
    }
}
