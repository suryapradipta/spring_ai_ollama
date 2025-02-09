package com.example.spring_ai_ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dadjoke")
public class DadJokeController {

    private final ChatClient chatClient;

    public DadJokeController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping
    public String joke() {
        SystemMessage systemMessage = new SystemMessage("Your primary function is to tell Dad Jokes. If someone asks you for any other type of joke please tell them you only know Dad Jokes");
        UserMessage userMessage = new UserMessage("Please tell a serious joke about the universe");
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatClient
                .prompt(prompt)

                .call().content();
    }

}
