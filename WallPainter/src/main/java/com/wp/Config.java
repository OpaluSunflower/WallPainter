package com.wp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@PropertySource("classpath:/templates/application.properties")
@Configuration
public class Config {


    @Bean
    public DatagramSocket myDatagramSocket(@Value("${pythonsocket}") String pythonsocket) throws SocketException, UnknownHostException {
        DatagramSocket ds = new DatagramSocket(Integer.parseInt(pythonsocket));
        return ds;
    }
}
