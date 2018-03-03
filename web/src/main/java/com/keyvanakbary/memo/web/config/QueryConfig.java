package com.keyvanakbary.memo.web.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.keyvanakbary.memo.query",
    entityManagerFactoryRef = "queryEntityManagerFactory"
)
@ComponentScan("com.keyvanakbary.memo.query")
public class QueryConfig {
    @Bean(name = "queryDataSource")
    public DataSource readDataSource(@Value("${read-model.datasource.url}") String url,
                                     @Value("${read-model.datasource.username}") String username,
                                     @Value("${read-model.datasource.password}") String password,
                                     @Value("${read-model.datasource.driver-class-name}") String driver,
                                     @Value("${read-model.hibernate.c3p0.max-size}") int maxSize,
                                     @Value("${read-model.hibernate.c3p0.min-size}") int minSize,
                                     @Value("${read-model.hibernate.c3p0.test-query}") String testQuery,
                                     @Value("${read-model.hibernate.c3p0.idle-test-period}") int idleTestPeriod) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setInitialPoolSize(minSize);
        dataSource.setMinPoolSize(minSize);
        dataSource.setMaxPoolSize(maxSize);
        dataSource.setIdleConnectionTestPeriod(idleTestPeriod);
        dataSource.setPreferredTestQuery(testQuery);
        dataSource.setJdbcUrl(url);
        dataSource.setPassword(password);
        dataSource.setUser(username);
        dataSource.setDriverClass(driver);
        return dataSource;
    }

    @Bean(name = "queryEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier("queryDataSource") DataSource dataSource) {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        adapter.setShowSql(true);

        Properties properties = new Properties();
        properties.setProperty("hibernate.implicit_naming_strategy", "component-path");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPersistenceUnitName("read_model");
        factory.setJpaVendorAdapter(adapter);
        factory.setJpaProperties(properties);
        factory.setPackagesToScan("com.keyvanakbary.memo.query");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "queryPlatformTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("queryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
