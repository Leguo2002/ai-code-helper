package com.gu01e.aicodehelper.ai;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;

import java.util.List;

/**
 * @author 郭乐
 */

public interface AiCodeHelperService {

    //会话记忆
    @SystemMessage(fromResource = "prompt//system-prompt.txt")
    String chat(String userMessage);

    //结构化输出
    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Report chatForReport(String userMessage);

    //RAG
    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Result<String> chatWithRag(String userMessage);



    //结构化输出格式
    /*@Value
        class report {
            String name;
            List<String> suggestionList;
        }*/
    record Report(String name, List<String> suggestionList) {
    }

}
