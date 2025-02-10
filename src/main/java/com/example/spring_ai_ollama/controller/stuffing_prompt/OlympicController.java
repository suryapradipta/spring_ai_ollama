package com.example.spring_ai_ollama.controller.stuffing_prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/olympic")
public class OlympicController {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/olympic-sports.st")
    private Resource olympictSportsResource;

    @Value("classpath:/docs/olympic-sports.txt")
    private Resource docsToStuffResource;

    public OlympicController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/2024")
    public String get2024OlympicSports(
            @RequestParam(value = "message", defaultValue = "What sports are being included in the 2024 Summer Olympics?") String message,
            @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit
    ) {

        PromptTemplate promptTemplate = new PromptTemplate(olympictSportsResource);
        Map<String, Object> map = new HashMap<>();
        map.put("question", message);
        if (stuffit) {
            map.put("context", docsToStuffResource);
        } else {
            map.put("context", "");
        }

        Prompt prompt = promptTemplate.create(map);
        ChatClient.CallResponseSpec call = chatClient.prompt(prompt).call();

        return call.content();
    }
}
