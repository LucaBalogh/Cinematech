package ro.ubb.tt.dal;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages= "ro.ubb.tt.*")
@EntityScan(basePackages="ro.ubb.tt.*")
public class EntitiesConfiguration {
}
