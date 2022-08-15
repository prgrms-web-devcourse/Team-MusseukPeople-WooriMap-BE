package com.musseukpeople.woorimapnotification.common.config.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;
import redis.embedded.exceptions.EmbeddedRedisException;

@Slf4j
@Configuration
@Profile(value = {"test", "local"})
public class EmbeddedRedisConfig {

    private static final String LOCAL_HOST = "127.0.0.1";
    private static final String BIN_SH = "/bin/sh";
    private static final String BIN_SH_WINDOW = "cmd.exe";
    private static final String BIN_SH_OPTION = "-c";
    private static final String BIN_SH_OPTION_WINDOW_ONE = "/y";
    private static final String BIN_SH_OPTION_WINDOW_TWO = "/c";
    private static final String COMMAND = "netstat -nat | grep LISTEN|grep %d";
    private static final String COMMAND_WINDOW = "netstat -nao | findstr %d";
    private static final int START_PORT = 10000;
    private static final int END_PORT = 65535;

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(LOCAL_HOST, redisPort);
    }

    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
        redisServer = new RedisServer(port);
        redisServer = RedisServer.builder()
            .port(port)
            .setting("maxmemory 128M")
            .build();
        redisServer.start();
    }

    private int findAvailablePort() throws IOException {
        for (int tempPort = START_PORT; tempPort <= END_PORT; tempPort++) {
            Process process = executeGrepProcessCommand(tempPort);
            if (!isRunning(process)) {
                return tempPort;
            }
        }
        throw new EmbeddedRedisException("내장 레디스 서버 에러");
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            log.error("내장 레디스 서버 에러 : {}", e.getMessage(), e);
        }
        return !pidInfo.toString().isEmpty();
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        if (isWindow()) {
            String command = String.format(COMMAND_WINDOW, port);
            String[] shell = {BIN_SH_WINDOW, BIN_SH_OPTION_WINDOW_ONE, BIN_SH_OPTION_WINDOW_TWO, command};
            return Runtime.getRuntime().exec(shell);
        }
        String command = String.format(COMMAND, port);
        String[] shell = {BIN_SH, BIN_SH_OPTION, command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isWindow() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}
