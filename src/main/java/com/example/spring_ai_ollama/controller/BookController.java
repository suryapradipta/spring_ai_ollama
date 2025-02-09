package com.example.spring_ai_ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/book")
public class BookController {

    private final ChatClient chatClient;

    public BookController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/by-author")
    public Author getBooksByAuthor(@RequestParam(value = "author", defaultValue = "J.K. Rowling") String author) {
        String message = """
                 Generate a list of books written by the author {author}. If you aren't positive with that a book
                 belongs to this author please don't include it.
                 {format}
                """;

        BeanOutputConverter<Author> outputConverter = new BeanOutputConverter<>(Author.class);

        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of(
                "author", author,
                "format", outputConverter.getFormat()
        ));

        return outputConverter.convert(requireNonNull(chatClient
                .prompt(promptTemplate.create())
                .call()
                .content()));
    }

    @GetMapping("/author/{author}")
    public Map<String, Object> getAuthorSocialLink(@PathVariable("author") String author) {
        String message = """
                 Generate a list of links for the author {author}.
                 Include the author's name as the key and social media links as a object.
                 {format}
                """;

        MapOutputConverter outputConverter = new MapOutputConverter();

        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of(
                "author", author,
                "format", outputConverter.getFormat()
        ));
        Prompt prompt = promptTemplate.create();

        return outputConverter.convert(requireNonNull(chatClient
                .prompt(prompt)
                .call()
                .content()));
    }


}
