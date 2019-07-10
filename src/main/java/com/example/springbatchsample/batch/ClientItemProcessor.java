package com.example.springbatchsample.batch;

import org.springframework.batch.item.ItemProcessor;

public class ClientItemProcessor implements ItemProcessor<ClientDto, ClientDto>{
	@Override
    public ClientDto process(ClientDto dto) throws Exception {
        
		final String clientId = dto.getClientId();
		final String clientName = dto.getClientName() + "_TEST";

		ClientDto updated = new ClientDto();
		updated.setClientId(clientId);
		updated.setClientName(clientName);
		
        return updated;
    }

}
