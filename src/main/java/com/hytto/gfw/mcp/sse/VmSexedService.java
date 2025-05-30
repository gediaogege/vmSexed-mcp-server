package com.hytto.gfw.mcp.sse;

import com.hytto.gfw.util.JsonUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qmt
 * @Data 2025/5/21 14:01
 */
@Service
public class VmSexedService {
    private final WebClient webClient;

    public VmSexedService(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create().secure(sslSpec -> sslSpec.sslContext(
            SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
        ));

        this.webClient = webClientBuilder
                .baseUrl("https://test10.lovense-api.com/surfease/common/sex/ed/article/spider/list")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Tool(description = "查询两性约会,性教育,性健康相关文章内容信息。输出标题-title,正文内容-content ,原文链接-link,查看更多-moreLink 等字段信息;最多给出3条最相关的文章")
    public String getSexEducationArticles(
            @ToolParam(description = "pageNo,每次从1到20之间随机一个值，不能每次查询pageNo都是1") Integer pageNo,
            @ToolParam(description = "pageSize,查询时每页条数，默认是3") Integer pageSize
    ) {

        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("pageNo",pageNo)
                            .queryParam("pageSize",pageSize)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            List<Map >list = new ArrayList<>();
            Map map = JsonUtil.jsonToMap(response);
            Map data = (Map) map.get("data");
            List<Map> resultList = (List<Map>) data.get("resultList");
            for (Map map1 : resultList) {
                if(map1!=null ){
                Map<String,String> result = new HashMap<>();
                    result.put("link", "https://www.vibemate.com/sex-ed/article-detail/"+map1.get("id"));
                    result.put("title", (String) map1.get("title"));
                    result.put("content", (String) map1.get("content"));
                    result.put("moreLink", "https://www.vibemate.com/sex-ed");
                    list.add(result);
                }
            }


            return  JsonUtil.toJson(list);

            //return response;
        } catch (Exception e) {
            return "根据消费者group查询出rocketmq在线的消费者信息失败：" + e.getMessage();
        }
    }
}