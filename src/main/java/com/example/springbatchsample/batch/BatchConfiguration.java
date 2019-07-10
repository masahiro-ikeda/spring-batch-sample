package com.example.springbatchsample.batch;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Bean
	public SqlSessionFactory sqlSessionFactory() {
		try {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource);
			return (SqlSessionFactory) sessionFactory.getObject();
		} catch (Exception e) {
			return null;
		}
	}

	@Bean
	public MyBatisCursorItemReader<ClientDto> reader() {
		return new MyBatisCursorItemReaderBuilder<ClientDto>()
				.sqlSessionFactory(sqlSessionFactory())
				.queryId("com.example.springbatchsample.batch.ClientMapper.selecter")
				.build();
	}

	@Bean
	public MyBatisBatchItemWriter<ClientDto> writer() {
		return new MyBatisBatchItemWriterBuilder<ClientDto>()
				.sqlSessionFactory(sqlSessionFactory())
				.statementId("com.example.springbatchsample.batch.ClientMapper.updater")
				.build();
	}

	//		@Bean
	//		public JdbcCursorItemReader<ClientDto> reader() {
	//			return new JdbcCursorItemReaderBuilder<ClientDto>()
	//					.name("reader")
	//					.dataSource(dataSource)
	//					.sql("SELECT client_id, client_name FROM clients")
	//					.rowMapper(new ClientRowMapper())
	//					.build();
	//		}
	//
	//	@Bean
	//	public ItemWriter<ClientDto> jdbcWriter(DataSource dataSource) {
	//		return new JdbcBatchItemWriterBuilder<ClientDto>()
	//				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	//				.sql("UPDATE clients SET client_name = :clientName WHERE client_id = :clientId")
	//				.dataSource(dataSource)
	//				.build();
	//	}

	@Bean
	public ClientItemProcessor clientProcessor() {
		return new ClientItemProcessor();
	}

	@Bean
	public Step updateClientStep(MyBatisBatchItemWriter<ClientDto> writer) {
		return stepBuilderFactory.get("updateClientStep")
				.<ClientDto, ClientDto> chunk(10)
				.reader(reader())
				.processor(clientProcessor())
				.writer(writer)
				.build();
	}

	@Bean(name = "sampleJob01")
	public Job updateClientJob(Step updateClientStep) {
		return jobBuilderFactory.get("updateClientJob")
				.incrementer(new RunIdIncrementer())
				.flow(updateClientStep)
				.end()
				.build();
	}
}
