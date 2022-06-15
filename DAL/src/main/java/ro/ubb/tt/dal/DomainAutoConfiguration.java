package ro.ubb.tt.dal;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(EntitiesConfiguration.class)
@ComponentScan(basePackages= "ro.ubb.tt.*")
public class DomainAutoConfiguration {
}
