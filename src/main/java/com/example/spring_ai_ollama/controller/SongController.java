package com.example.spring_ai_ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/song")
public class SongController {

    private final ChatClient chatClient;

    public SongController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/artist")
    public List<String> findArtistSong(@RequestParam(value = "artist", defaultValue = "Taylor Swift") String artist) {
        String message = """
                 Please give me the list of top 10 songs for the artist {artist}.
                 If you don't know the answer, you can say "I don't know".
                 {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(message);
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

        Prompt prompt = promptTemplate.create(Map.of(
                "artist", artist,
                "format", listOutputConverter.getFormat()
        ));
        return listOutputConverter.convert(Objects.requireNonNull(chatClient
                .prompt(prompt)
                .call()
                .content()));

    }

}
