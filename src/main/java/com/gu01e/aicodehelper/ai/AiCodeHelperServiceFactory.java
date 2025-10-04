package com.gu01e.aicodehelper.ai;

import com.gu01e.aicodehelper.ai.tools.InterviewQuestionTool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 郭乐
 */
@Configuration
public class AiCodeHelperServiceFactory {
    @Resource
    private ChatModel qwenChatModel;

//    @Resource
//    private ContentRetriever contentRetriever;

    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id("123")
                .maxMessages(10)
//                .chatMemoryStore(null)
                .build();
        return AiServices.builder(AiCodeHelperService.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory)
//                .contentRetriever(contentRetriever)
                .tools(new InterviewQuestionTool())
                .build();
    }
}
