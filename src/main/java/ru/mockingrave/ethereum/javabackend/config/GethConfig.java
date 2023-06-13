package ru.mockingrave.ethereum.javabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.ipc.WindowsIpcService;

@Configuration
public class GethConfig {

    @Bean
    public Web3j getWeb3j() {
        return Web3j.build(new WindowsIpcService("\\\\.\\pipe\\geth.ipc"));
    }

}

