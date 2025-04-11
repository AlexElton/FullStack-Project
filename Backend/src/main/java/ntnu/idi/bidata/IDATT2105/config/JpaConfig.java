package ntnu.idi.bidata.IDATT2105.config;


import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class JpaConfig {

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String ddlAuto;

  @Value("${spring.jpa.database-platform}")
  private String databasePlatform;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("ntnu.idi.bidata.IDATT2105.models");



    // Use Hibernate as the JPA provider
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setShowSql(true);
    em.setJpaVendorAdapter(vendorAdapter);

    // Set JPA properties
    // Properties properties = new Properties();
    // properties.putAll(DatabaseConfig.getPersistenceProperties());
    // em.setJpaProperties(properties);
    // TODO: Make databaseconfig class

    em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", ddlAuto);
    em.getJpaPropertyMap().put("hibernate.dialect", databasePlatform);


    return em;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
    
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
    final DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    initializer.setDatabasePopulator(populator);
    return initializer;
  }

}