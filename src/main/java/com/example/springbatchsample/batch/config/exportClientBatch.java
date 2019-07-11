package com.example.springbatchsample.batch.config;

import com.example.springbatchsample.batch.entity.Client;
import com.example.springbatchsample.batch.processor.ImportClientProcessor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/*
 * 取引先データ出力バッチ
 * - 各Beanの構成は次の通り
 *
 *   Job - Step(*) - reader
 *                 - processor
 *                 - writer
 *
 *   (*)JobとStepは1:nで設定できます
 */
@Configuration
@EnableBatchProcessing
public class exportClientBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // 出力ファイル生成用の定数データ
    private final static String CHARACTER_CODE_UTF8 = "UTF-8";
    private final static String COMMA = ",";
    private final static String SEPARATOR = System.lineSeparator();

    /*
     * エクスポート用のジョブ設定
     */
    @Bean(name = "exportClient")
    public Job exportClientJob(Step exportClientStep) {
        return jobBuilderFactory.get("exportClientJob")
                .incrementer(new RunIdIncrementer())
                .flow(exportClientStep)
                .end()
                .build();
    }

    /*
     * エクスポート用のステップ設定
     */
    @Bean
    public Step exportClientStep(FlatFileItemWriter<Client> writer) {
        return stepBuilderFactory.get("exportClientStep")
                .<Client, Client>chunk(10)
                .reader(exportClientReader())
                .writer(writer)
                .build();
    }

    /*
     * 出力データをDBから取得
     */
    @Bean
    public MyBatisCursorItemReader<Client> exportClientReader() {
        return new MyBatisCursorItemReaderBuilder<Client>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.springbatchsample.batch.mapper.ClientMapper.clientSelecter")
                .build();
    }

    /*
     * CSVフ ァイル出力
     */
    @Bean
    public FlatFileItemWriter<Client> exportClientsWriter() {
        return new FlatFileItemWriterBuilder<Client>()
                .name("exportClientWriter")
                .resource(new ClassPathResource("files/export-client.csv"))
                .lineAggregator(item -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(item.getClientId());
                    sb.append(COMMA);
                    sb.append(item.getClientName());
                    sb.append(COMMA);
                    sb.append(item.getSalesStaffNo());
                    sb.append(COMMA);
                    sb.append(item.getIsInvalid());
                    sb.append(COMMA);
                    sb.append(item.getUpdaterId());
                    sb.append(COMMA);
                    sb.append(item.getUpdatedAt());
                    sb.append(COMMA);
                    sb.append(item.getCreaterId());
                    sb.append(COMMA);
                    sb.append(item.getCreatedAt());
                    return sb.toString();
                })
                .lineSeparator(SEPARATOR)
                .encoding(CHARACTER_CODE_UTF8) // 文字コード指定
                .build();
    }
}
