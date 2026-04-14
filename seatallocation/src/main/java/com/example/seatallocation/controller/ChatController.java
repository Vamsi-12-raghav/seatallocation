package com.example.seatallocation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.seatallocation.dto.ChatRequest;
import com.example.seatallocation.dto.ChatResponse;
import com.example.seatallocation.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    @ResponseStatus(HttpStatus.OK)
    public ChatResponse askAI(@RequestBody ChatRequest request) {
        String response = chatService.askAI(request.getMessage(), request.getRollNo());
        return new ChatResponse(response);
    }
}
