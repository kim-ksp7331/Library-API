package ksp7331.practice.libraryAPI.config;

import ksp7331.practice.libraryAPI.QueryDslConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(QueryDslConfig.class)
@ComponentScan
public class DbTestConfig {

}
