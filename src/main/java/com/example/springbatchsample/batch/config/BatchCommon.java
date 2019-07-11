package com.example.springbatchsample.batch.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/*
 * バッチ実行の共通設定を記述するクラス
 */
@Configuration
public class BatchCommon {

    @Autowired
    private DataSource dataSource;

    /*
     * MyBatisの実行に必要な接続データ生成クラスの設定
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            return sessionFactory.getObject();
        } catch (Exception e) {
            return null;
        }
    }
}
