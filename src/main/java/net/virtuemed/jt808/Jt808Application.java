package net.virtuemed.jt808;

import net.virtuemed.jt808.server.NettyTcpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Jt808Application implements CommandLineRunner {

    @Autowired
    private NettyTcpServer server;

    public static void main(String[] args) {
        SpringApplication.run(Jt808Application.class, args);
    }

    @Override
    public void run(String... args) {
        server.start();
    }


}
