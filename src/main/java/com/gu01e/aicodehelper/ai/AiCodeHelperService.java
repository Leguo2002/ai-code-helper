package com.gu01e.aicodehelper.ai;

import com.gu01e.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author 郭乐
 */
@InputGuardrails({SafeInputGuardrail.class})
public interface AiCodeHelperService {

    //会话记忆
    @SystemMessage(fromResource = "prompt//system-prompt.txt")
    String chat(@UserMessage String userMessage);

    //结构化输出
    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Report chatForReport(@UserMessage String userMessage);

    //RAG
    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Result<String> chatWithRag(@UserMessage String userMessage);

    //流式输出
    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);


    //结构化输出格式
    /*@Value
        class report {
            String name;
            List<String> suggestionList;
        }*/
    record Report(String name, List<String> suggestionList) {
    }

}
