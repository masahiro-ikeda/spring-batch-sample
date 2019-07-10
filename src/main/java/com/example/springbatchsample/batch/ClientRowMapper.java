package com.example.springbatchsample.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClientDto dto = new ClientDto();

		dto.setClientId(rs.getString("client_id"));
		dto.setClientName(rs.getString("client_name"));

		return dto;
	}
}
