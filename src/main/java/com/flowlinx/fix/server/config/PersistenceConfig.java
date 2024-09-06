package com.flowlinx.fix.server.config;

import com.flowlinx.fix.server.service.AwsSecretsManagerService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EntityScan("com.flowlinx.fix.server.domain")
@EnableJpaRepositories("com.flowlinx.fix.server.repository")
public class PersistenceConfig {

    public static final String ENTITY_PACKAGE = "com.flowlinx.fix.server";

    @Value("${datasource.driver}")
    private String driver;
    
//    @Value("${datasource.url}")
//    private String url;
//
//    @Value("${datasource.username}")
//    private String username;
//
//    @Value("${datasource.password}")
//    private String password;

    private String url;

    private String username;

    private String password;


    @Value("${datasource.hbm2ddlAuto}")
    private String hbm2ddlAuto;
    
    @Value("${datasource.hibernateShowSql}")
    private String hibernateShowSql;
    
    @Value("${datasource.dialect}")
    private String dialect;
    
    @Value("${datasource.contextual.lob.creation}")
    private Boolean contextualLobCreation;
    
    @Value("${datasource.dbcp.maxTotalConnections}")
    private int maxTotalConnections;

    @Value("${datasource.dbcp.maxIdleConnections}")
    private int maxIdleConnections;

    @Value("${flyway.migration.locations}")
    private String[] migrations;

    @Value("${flyway.migration.table}")
    private String migrationTable;

    @Autowired
    private AwsSecretsManagerService awsSecretsManagerService;

    @PostConstruct
    private void init() {
        Map<String, String> secret = awsSecretsManagerService.getSecret();
        this.url = "jdbc:postgresql://" + secret.get("host") + ":" + secret.get("port") + "/" + secret.get("dbname") + "?socketTimeout=30";
        this.username = secret.get("username");
        this.password = secret.get("password");
    }

    @Bean
    public DataSource dataSource() {
        final BasicDataSource datasource = new BasicDataSource();
        datasource.setDriverClassName(driver);
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setMaxTotal(maxTotalConnections);
        datasource.setMaxIdle(maxIdleConnections);
        datasource.setPoolPreparedStatements(true);
        return datasource;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager( entityManagerFactory );
    }

    @Bean
    @Autowired
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        final TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        return transactionTemplate;
    }

    @Bean
    @Autowired
    @DependsOn({"flyway"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(Flyway flyway) {

        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl( Boolean.TRUE );
        vendorAdapter.setShowSql( Boolean.valueOf( hibernateShowSql ) );
        factory.setDataSource( dataSource() );
        factory.setJpaVendorAdapter( vendorAdapter );
        factory.setPackagesToScan( ENTITY_PACKAGE );
        factory.setPersistenceProvider( new HibernatePersistenceProvider() );

        final Properties jpaProperties = new Properties();
        jpaProperties.put( Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        jpaProperties.put( Environment.DIALECT, dialect);
        jpaProperties.put( Environment.NON_CONTEXTUAL_LOB_CREATION, contextualLobCreation );
        // jpaProperties.put( Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getCanonicalName() );

        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();

        return factory;
    }

    @Autowired
    @Bean(name = "flyway", initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {

        return Flyway.configure()
                .dataSource( dataSource )
                .locations( migrations )
                .validateOnMigrate( false )
                .outOfOrder( true )
                .baselineOnMigrate( true )
                .baselineVersion("0")
                .placeholderPrefix( "##${" )
                .placeholderSuffix( "}" )
                .table( migrationTable )
                .load();
    }

}