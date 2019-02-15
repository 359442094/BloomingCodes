package cn.blooming.bep.crawler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  
                .apiInfo(apiInfo())  
                .select()  
                .apis(RequestHandlerSelectors.any())// 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外  
                .paths(PathSelectors.any())  
                .build();  
	}
	
    private ApiInfo apiInfo() {// 创建API的基本信息，这些信息会在Swagger UI中进行显示  
        return new ApiInfoBuilder()  
                .title("基金数据平台")// API 标题
                .description("基金数据平台API接口")// API描述
                .version("1.0")// 版本号  
                .build();  
    }  
	
}
