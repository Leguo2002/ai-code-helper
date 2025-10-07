package com.gu01e.aicodehelper.controller;

import com.gu01e.aicodehelper.ai.AiCodeHelperService;
import jakarta.annotation.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @className: AiController
 * @Description: TODO
 * @version: v1.0
 * @author: GUOLE
 * @date: 2025/10/10 16:26
 */
@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @RequestMapping("/chat")
//    SSE接口
    public Flux<ServerSentEvent<String>> chat(int memoryId, String message) {
        return aiCodeHelperService.chatStream(memoryId, message)
                .map(chunk -> ServerSentEvent
                        .<String>builder()
                        .data(chunk)
                        .build());
    }

}