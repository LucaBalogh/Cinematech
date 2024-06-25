package ro.uoradea.dal;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages= "ro.uoradea.*")
@EntityScan(basePackages="ro.uoradea.*")
public class EntitiesConfiguration {
}
