package st.catalog;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class CataloggerConfig
{
    @Value("${st.show-sql:true}")
    private String showSQL;

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;IGNORECASE=TRUE");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(@Qualifier("dataSource") DataSource dataSource)
    {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan(new String[]{"com.rxbenefits"});
        sessionFactoryBean.setHibernateProperties(this.hibernateProperties());
        return sessionFactoryBean;
    }

    protected Properties hibernateProperties()
    {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.put("hibernate.show_sql", this.showSQL);
        hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
        return hibernateProperties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("sessionFactory") LocalSessionFactoryBean sessionFactory)
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();

        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }
}
