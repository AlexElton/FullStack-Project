package ntnu.idi.bidata.IDATT2105.configs;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
public class JpaConfig {
  
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("ntnu.idi.bidata.IDATT2105.models");
    
    // TODO Add JPA properties here if needed
    
    return em;
  }
}
