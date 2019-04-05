package com.spacex.octopus.configuration;


import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.spacex.octopus.algorithm.DatabaseShardingAlgorithm;
import com.spacex.octopus.algorithm.TableShardingAlgorithm;
import com.spacex.octopus.constants.DataSourceConstants;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(DataSourceConstants.DATASOURCE_PREFIX)
@MapperScan(basePackages = DataSourceConstants.MAPPER_PACKAGE, sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class DataSourceConfig {
    private String url;
    private String username;
    private String password;

    /**
     * import tk.mybatis.spring.annotation.MapperScan
     * 不然扫描不到PO实体类
     **/
    @Bean(name = "mybatisDataSource")
    public DataSource getDataSource() throws SQLException {

        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

        dataSourceMap.put("magellan_0", mybatisDataSource("magellan_0"));
        dataSourceMap.put("magellan_1", mybatisDataSource("magellan_1"));

        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "magellan_0");

        TableRule tableRule = TableRule
                .builder("shop")
                //.generateKeyColumn("id")
                .actualTables(Arrays.asList("shop_0", "shop_1"))
                .dataSourceRule(dataSourceRule)
                .build();

        Long tableShardingCount = 2L;
        Long databaseShardingCount = 2L;

        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Collections.singletonList(tableRule))
                .databaseShardingStrategy(new DatabaseShardingStrategy("id", new DatabaseShardingAlgorithm(databaseShardingCount)))
                .tableShardingStrategy(new TableShardingStrategy("id", new TableShardingAlgorithm(tableShardingCount))).build();

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        return dataSource;
    }

    private DataSource mybatisDataSource(final String dataSourceName) throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(DataSourceConstants.DRIVER_CLASS);
        dataSource.setJdbcUrl(String.format(url, dataSourceName));
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setAutoCommit(true);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaximumPoolSize(50);
        dataSource.setConnectionInitSql("SELECT 'x'");
        dataSource.setConnectionTestQuery("SELECT 'x'");
        dataSource.setMinimumIdle(300000);

        return dataSource;
    }

    /**
     * Sharding-jdbc的事务支持
     *
     * @return
     */
    @Bean(name = "mybatisTransactionManager")
    public DataSourceTransactionManager mybatisTransactionManager(@Qualifier("mybatisDataSource") DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "mybatisSqlSessionFactory")
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mybatisDataSource") DataSource mybatisDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mybatisDataSource);
        return sessionFactory.getObject();
    }

//    @Bean
//    public IdGenerator getIdGenerator() {
//        return new CommonSelfIdGenerator();
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
