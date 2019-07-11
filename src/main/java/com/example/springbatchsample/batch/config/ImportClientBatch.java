package com.example.springbatchsample.batch.config;

import com.example.springbatchsample.batch.entity.Client;
import com.example.springbatchsample.batch.processor.ImportClientProcessor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/*
 * 取引先データ取込バッチ
 * TODO
 *  データの形式・整合性チェックの追加が必要
 */
@Configuration
@EnableBatchProcessing
public class ImportClientBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // 読込データにヘッダキーを付与
    private final static String[] importColumns = {
            "clientId",
            "clientName",
            "salesStaffNo",
            "isInvalid"
    };
    // ファイル読込時に付与する文字コード
    private final static String CHARACTER_CODE_UTF8 = "UTF-8";

    /*
     * インポート用のジョブ設定
     */
    @Bean(name = "importClient")
    public Job importClientJob(Step importClientStep) {
        return jobBuilderFactory.get("importClientJob")
                .incrementer(new RunIdIncrementer())
                .flow(importClientStep)
                .end()
                .build();
    }

    /*
     * インポート用のステップ設定
     */
    @Bean
    public Step importClientStep(MyBatisBatchItemWriter<Client> writer) {
        return stepBuilderFactory.get("importClientStep")
                .<Client, Client>chunk(10)
                .reader(importClientsReader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    /*
     * CSVファイルの読込
     */
    @Bean
    public FlatFileItemReader<Client> importClientsReader() {
        return new FlatFileItemReaderBuilder<Client>()
                .name("importClientReader")
                .resource(new ClassPathResource("files/import-client.csv"))
                .delimited()
                .names(importColumns)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Client>() {{
                    setTargetType(Client.class);
                }})
                .encoding(CHARACTER_CODE_UTF8) // 文字コード指定
                .build();
    }

    /*
     * 読込データをDB保存するためのBean生成
     */
    @Bean
    public MyBatisBatchItemWriter<Client> importClientsWriter() {
        return new MyBatisBatchItemWriterBuilder<Client>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.springbatchsample.batch.mapper.ClientMapper.clientInserter")
                .build();
    }

    /*
     * 中間処理を記述したクラスのBeanを生成
     */
    @Bean
    public ImportClientProcessor processor() {
        return new ImportClientProcessor();
    }
}
