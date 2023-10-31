package com.wp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.DatagramSocket;
import java.net.SocketException;

@Configuration
public class Config {


    @Bean
    public DatagramSocket myDatagramSocket(@Value("pythonsocket") int pythonsocket) throws SocketException {

        return new DatagramSocket(pythonsocket);
    }
}
