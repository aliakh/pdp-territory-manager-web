package demo;

import org.springframework.orm.jpa.JpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Data source.
 */
public interface DataSourceConfig {

    DataSource dataSource();

    JpaVendorAdapter jpaVendorAdapter();
}
