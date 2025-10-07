package com.gu01e.aicodehelper.ai;

import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void testChatWithMemory() {
        String message1 = "我是程序员gu01e";
        String result1 = aiCodeHelperService.chat(message1);
        System.out.println(result1);
        String message2 = "请问我是谁";
        String result2 = aiCodeHelperService.chat(message2);
        System.out.println(result2);
    }

    @Test
    void testChatForReport() {
        String message = "我是程序员gu01e，学习编程一年半，请你帮我生成一个学习报告";
        AiCodeHelperService.Report report = aiCodeHelperService.chatForReport(message);
        System.out.println(report);
    }

    @Test
    void testChatWithRag() {
        String message = "我现在正在准备java反射相关的面试题，请你帮我查找一些出来";
        Result<String> result = aiCodeHelperService.chatWithRag(message);
        System.out.println(result.sources());
        System.out.println(result.content());
    }

    @Test
    void testInterviewQuestionSearch() {
        String result = aiCodeHelperService.chat("请你帮我查找一些计算机网络的热门面试题");
        System.out.println("----------------------------------------------");
        System.out.println(result);
    }
    @Test
    void chatWithMcp() {
        String result = aiCodeHelperService.chat("什么是程序员鱼皮的编程导航？");
        System.out.println(result);
    }

}