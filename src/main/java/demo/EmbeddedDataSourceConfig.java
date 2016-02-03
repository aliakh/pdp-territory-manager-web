package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Embedded data source.
 */
@Configuration
@Profile("development")
public class EmbeddedDataSourceConfig implements DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2)
                .addScript("/db/ddl.sql")
                .addScript("/db/CA/cdd_franchise.sql")
                .addScript("/db/CA/zip_code.sql")
                .addScript("/db/CA/dealer_pdp_region.0.sql")
                .addScript("/db/CA/dealer_pdp_region.1.sql")
                .addScript("/db/CA/dealer_pdp_region.2.sql")
                .addScript("/db/CA/dealer_pdp_region.3.sql")
                .addScript("/db/CA/dealer_pdp_region.4.sql")
                .addScript("/db/CA/dealer_pdp_region.5.sql")
                .addScript("/db/CA/dealer_pdp_region.6.sql")
                .addScript("/db/CA/dealer_pdp_region.7.sql")
                .addScript("/db/CA/dealer_pdp_region.8.sql")
                .addScript("/db/CA/dealer_pdp_region.9.sql")
                .addScript("/db/CA/dealer_pdp_zip_region.sql");
        return builder.build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        jpaVendorAdapter.setGenerateDdl(false);
        return jpaVendorAdapter;
    }
}
