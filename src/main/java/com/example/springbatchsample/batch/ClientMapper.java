package com.example.springbatchsample.batch;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientMapper {

	public List<ClientDto> selecter();

	public void updater(ClientDto dto);
}
