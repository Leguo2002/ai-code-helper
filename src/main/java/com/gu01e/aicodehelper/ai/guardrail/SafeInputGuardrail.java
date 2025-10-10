package com.gu01e.aicodehelper.ai.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @className: SafeInputGuardrail
 * @Description: 输入安全检测
 * @version: v1.0
 * @author: GUOLE
 * @date: 2025/10/7 22:25
 */
public class SafeInputGuardrail implements InputGuardrail {

    private static final Set<String> sensitiveWords = loadSensitiveWords();

    private static Set<String> loadSensitiveWords() {
        Set<String> words = new HashSet<>();
        try (InputStream inputStream = SafeInputGuardrail.class.getClassLoader()
                .getResourceAsStream("words/sensitive-words.txt");
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 按换行符分割，去除空白字符后添加到集合中
                String word = line.trim();
                if (!word.isEmpty()) {
                    words.add(word.toLowerCase());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sensitive words from sensitive-words.txt", e);
        }
        return words;
    }

    /**
     * 检测用户输入是否安全
     */
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        // 获取用户输入并转换为小写以确保大小写不敏感
        String inputText = userMessage.singleText().toLowerCase();
        // 使用正则表达式分割输入文本为单词
        String[] words = inputText.split("\\W+");
        // 遍历所有单词，检查是否存在敏感词
        for (String word : words) {
            if (sensitiveWords.contains(word)) {
                return fatal("Sensitive word detected: " + word);
            }
        }
        return success();
    }
}
