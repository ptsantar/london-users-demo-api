package london.users.demo.api.config;

import london.users.demo.api.model.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.any;
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("london.users.demo.api"))
                .paths(any())
                .build()
                .apiInfo(metaData())
                .pathMapping("/");
    }
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("London Users API")
                .description("API which calls a 3rd-party API at https://bpdts-test-app.herokuapp.com/, " +
                        "and returns people who are listed as either living in a specified city (e.g. London), or whose " +
                        "current coordinates are within 50 miles of it.")
                .version("1.0")
                .contact(new Contact(
                        "Paraskevas Tsantarliotis",
                        "https://github.com/ptsantar/",
                        "-"))

                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}