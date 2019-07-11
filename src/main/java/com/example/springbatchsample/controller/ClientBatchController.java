package com.example.springbatchsample.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("client")
public class ClientBatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importClient; // インポートのジョブ

    @Autowired
    private Job exportClient; // エクスポートのジョブ

    /*
     * 取引先データのimport実行
     */
    @GetMapping("import")
    public String requestImportClientJob() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        jobLauncher.run(importClient, createInitialJobParameterMap());
        return "success import";
    }

    /*
     * 取引先データのexport実行
     */
    @GetMapping("export")
    public String requestExportClientJob() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        jobLauncher.run(exportClient, createInitialJobParameterMap());
        return "success export";
    }

    /*
     * ジョブパラメータの作成
     * - 今回は起動するだけなので最低限のパラメータ生成のみ
     */
    private JobParameters createInitialJobParameterMap() {
        Map<String, JobParameter> m = new HashMap<>();
        m.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(m);
        return jobParameters;
    }
}

// TODO:listnerを追加して例外処理を行う
// https://terasoluna-batch.github.io/guideline/5.0.0.RELEASE/ja/Ch06_ExceptionHandling.html
class batchResult{
    public String status;
    public ErrorMessage error;
}

class ErrorMessage{
    public String errorType;
    public String message;
}