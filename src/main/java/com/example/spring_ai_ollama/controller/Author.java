package com.example.spring_ai_ollama.controller;

public class Author {
    private String name;
    private String[] books;

    public Author(String name, String[] books) {
        this.name = name;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public String[] getBooks() {
        return books;
    }
}
