package com.spacex.octopus.configuration;


import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.NoneDatabaseShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.NoneTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.spacex.octopus.constants.DataSourceConstants;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

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

    @Bean(name = "mybatisDataSource")
    public DataSource getDataSource() throws SQLException {

        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

        dataSourceMap.put("springboot_0", mybatisDataSource("springboot"));
        dataSourceMap.put("springboot_1", mybatisDataSource("springboot2"));

        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "springboot_0");

        TableRule tableRule = TableRule
                .builder("user")
                .generateKeyColumn("user_id")
                .actualTables(Arrays.asList("user_0", "user_1"))
                .dataSourceRule(dataSourceRule)
                .build();


        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Collections.singletonList(tableRule))
                .databaseShardingStrategy(new DatabaseShardingStrategy("city_id", new NoneDatabaseShardingAlgorithm()))
                .tableShardingStrategy(new TableShardingStrategy("user_id", new NoneTableShardingAlgorithm())).build();

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        return dataSource;
    }

    private DataSource mybatisDataSource(final String dataSourceName) throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(DataSourceConstants.DRIVER_CLASS);
        dataSource.setJdbcUrl(String.format(url, dataSourceName));
        dataSource.setUsername(username);
        dataSource.setPassword(password);

//        /* 配置初始化大小、最小、最大 */
//        dataSource.setInitialSize(1);
//        dataSource.setMinIdle(1);
//        dataSource.setMaxActive(20);
//
//        /* 配置获取连接等待超时的时间 */
//        dataSource.setMaxWait(60000);
//
//        /* 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
//        dataSource.setTimeBetweenEvictionRunsMillis(60000);
//
//        /* 配置一个连接在池中最小生存的时间，单位是毫秒 */
//        dataSource.setMinEvictableIdleTimeMillis(300000);
//
//        dataSource.setValidationQuery("SELECT 'x'");
//        dataSource.setTestWhileIdle(true);
//        dataSource.setTestOnBorrow(false);
//        dataSource.setTestOnReturn(false);
//
//        /* 打开PSCache，并且指定每个连接上PSCache的大小。
//           如果用Oracle，则把poolPreparedStatements配置为true，
//           mysql可以配置为false。分库分表较多的数据库，建议配置为false */
//        dataSource.setPoolPreparedStatements(false);
//        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
//
//        /* 配置监控统计拦截的filters */
//        dataSource.setFilters("stat,wall,log4j");
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
