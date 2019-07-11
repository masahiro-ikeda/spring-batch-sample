package com.example.springbatchsample.batch.processor;

import com.example.springbatchsample.batch.entity.Client;
import org.springframework.batch.item.ItemProcessor;

import java.sql.Timestamp;

public class ImportClientProcessor implements ItemProcessor<Client, Client> {

    /*
     * 中間処理
     * - csvファイルから取り込んだデータをDB保存用に加工する
     * - サンプルのため保存データの追加のみ実施
     */
    @Override
    public Client process(Client client) throws Exception {

        // 更新・作成ユーザ ( サンプルのため直接記述 )
        String userId = "admin";
        // 現在時刻
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // csvから読み込んでいないデータをセット
        client.setCreaterId(userId);
        client.setCreatedAt(currentTime);
        client.setUpdaterId(userId);
        client.setUpdatedAt(currentTime);

        return client;
    }

}
