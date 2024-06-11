package yoon.mc.memitService.config;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi memberApiGroup(){
        return GroupedOpenApi.builder()
                .group("Member API")
                .pathsToMatch("/api/v1/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi postApiGroup(){
        return GroupedOpenApi.builder()
                .group("Post API")
                .pathsToMatch("/api/v1/posts/**")
                .build();
    }
}
