package com.hytto.gfw.mcp;

import com.hytto.gfw.mcp.sse.VmSexedService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class VmMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(VmMcpApplication.class, args);
    }
    @Bean
    public ToolCallbackProvider weatherTools(VmSexedService mpcService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(mpcService)
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {


        return WebClient.builder();
    }
}
