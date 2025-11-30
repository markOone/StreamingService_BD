package dev.studentpp1.streamingservice;

import org.springframework.boot.SpringApplication;

public class TestStreamingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(StreamingServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
