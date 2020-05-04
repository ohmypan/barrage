package com.huahong.barrage;

import com.huahong.barrage.server.WebsocketBarrageServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huahong
 */
@SpringBootApplication
public class BarrageApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(BarrageApplication.class, args);

        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8081;
        }
        new WebsocketBarrageServer(port).run();
    }

}
