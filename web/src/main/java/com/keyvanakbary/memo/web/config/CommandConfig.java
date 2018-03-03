package com.keyvanakbary.memo.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.NoCache;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.jpa.JpaSagaStore;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class CommandConfig {
    @Bean
    @Primary
    public DataSource writeDataSource(@Value("${write-model.datasource.url}")String url,
                                      @Value("${write-model.datasource.username}") String username,
                                      @Value("${write-model.datasource.password}") String password,
                                      @Value("${write-model.datasource.driver-class-name}")String driver,
                                      @Value("${write-model.hibernate.c3p0.max-size}") int maxSize,
                                      @Value("${write-model.hibernate.c3p0.min-size}") int minSize,
                                      @Value("${write-model.hibernate.c3p0.test-query}") String testQuery,
                                      @Value("${write-model.hibernate.c3p0.idle-test-period}") int idleTestPeriod) throws PropertyVetoException {
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

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        adapter.setShowSql(true);

        Properties properties = new Properties();
        properties.setProperty("hibernate.implicit_naming_strategy", "component-path");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(adapter);
        factory.setJpaProperties(properties);
        factory.setPackagesToScan("org.axonframework");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public Serializer serializer() {
        return new JacksonSerializer(new ObjectMapper());
    }

    @Bean
    @Primary
    public SagaStore<Object> sagaStore(Serializer serializer, EntityManagerProvider entityManagerProvider) {
        return new JpaSagaStore(serializer, entityManagerProvider);
    }

    @Bean
    @Primary
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    @Bean
    @Primary
    public EventStorageEngine eventStorageEngine(Serializer serializer,
                                                 DataSource dataSource,
                                                 EntityManagerProvider entityManagerProvider,
                                                 TransactionManager transactionManager) throws SQLException {
        return new JpaEventStorageEngine(serializer, null, dataSource, entityManagerProvider, transactionManager);
    }

    @Bean
    @Primary
    public TokenStore tokenStore(Serializer serializer, EntityManagerProvider entityManagerProvider) {
        return new JpaTokenStore(entityManagerProvider, serializer);
    }

    @Bean
    public SpringAggregateSnapshotterFactoryBean springAggregateSnapshotterFactoryBean() {
        return new SpringAggregateSnapshotterFactoryBean();
    }

    @Bean
    public Snapshotter snapshotter(SpringAggregateSnapshotterFactoryBean factory,
                                   PlatformTransactionManager platformTransactionManager,
                                   EventStore eventStore) throws Exception {
        factory.setTransactionManager(platformTransactionManager);
        factory.setEventStore(eventStore);
        return factory.getObject();
    }

    @Bean
    @Primary
    public EventBus eventBus(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

    @Bean
    public Cache noCache() {
        return NoCache.INSTANCE;
    }

    @Bean
    public EventStore eventStore(EventBus eventBus) {
        return (EventStore) eventBus;
    }

}
