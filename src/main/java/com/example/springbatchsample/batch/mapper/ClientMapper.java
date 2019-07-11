package com.example.springbatchsample.batch.mapper;

import java.util.List;

import com.example.springbatchsample.batch.entity.Client;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientMapper {

    /*
     * 取引先データの取得
     *
     * @return 取引先データ一覧
     */
    List<Client> clientSelecter();

    /*
     * 取引先データの登録
     *
     * @param 取引先データ
     */
    void clientInserter(Client dto);
}
