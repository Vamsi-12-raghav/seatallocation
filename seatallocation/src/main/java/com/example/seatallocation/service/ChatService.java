package com.example.seatallocation.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.seatallocation.entity.Seating;
import com.example.seatallocation.repository.SeatingRepository;

@Service
public class ChatService {

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    private final SeatingRepository seatingRepository;
    private final RestTemplate restTemplate;

    public ChatService(SeatingRepository seatingRepository) {
        this.seatingRepository = seatingRepository;
        this.restTemplate = new RestTemplate();
    }

    public String askAI(String userMessage, String rollNo) {
        // If no API key is configured, use rule-based response
        if (openaiApiKey == null || openaiApiKey.isEmpty() || openaiApiKey.equals("your-api-key-here")) {
            return generateSmartResponse(userMessage, rollNo);
        }

        try {
            return callOpenAI(userMessage, rollNo);
        } catch (Exception e) {
            // Fallback to rule-based response if API call fails
            return generateSmartResponse(userMessage, rollNo);
        }
    }

    private String callOpenAI(String userMessage, String rollNo) {
        try {
            // Build context
            String context = "You are a helpful AI assistant for an exam seating allocation system. ";
            if (rollNo != null && !rollNo.isEmpty()) {
                Optional<Seating> seating = seatingRepository.findByRollNo(rollNo);
                if (seating.isPresent()) {
                    Seating seat = seating.get();
                    context += String.format(
                            "The student with roll number %s is allocated to Hall %s, Seat %s. ",
                            rollNo, seat.getHallNumber(), seat.getSeatNumber());
                }
            }
            context += "Answer questions about exam seating allocations, exam procedures, and general exam information. Keep responses concise.";

            // Create request body
            String requestBody = String.format(
                    "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"system\",\"content\":\"%s\"},{\"role\":\"user\",\"content\":\"%s\"}],\"max_tokens\":150,\"temperature\":0.7}",
                    escapeJson(context),
                    escapeJson(userMessage));

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);

            // Make request
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    String.class);

            // Extract response text (simple parsing)
            if (response != null && response.contains("\"content\"")) {
                int startIndex = response.indexOf("\"content\":\"") + 11;
                int endIndex = response.indexOf("\"", startIndex);
                if (endIndex > startIndex) {
                    return unescapeJson(response.substring(startIndex, endIndex));
                }
            }

            return generateSmartResponse(userMessage, rollNo);
        } catch (Exception e) {
            return generateSmartResponse(userMessage, rollNo);
        }
    }

    private String generateSmartResponse(String userMessage, String rollNo) {
        String lowerMessage = userMessage.toLowerCase();

        // Check user's seat allocation if roll number provided
        if (rollNo != null && !rollNo.isEmpty()) {
            if (lowerMessage.contains("seat") || lowerMessage.contains("hall") || lowerMessage.contains("where")
                    || lowerMessage.contains("allocation") || lowerMessage.contains("exam hall")) {
                Optional<Seating> seating = seatingRepository.findByRollNo(rollNo);
                if (seating.isPresent()) {
                    Seating seat = seating.get();
                    return String.format(
                            "✓ Your seat allocation:\n\n🏛️ **Hall:** %s\n🪑 **Seat:** %s\n\nPlease arrive 15 minutes before your exam starts. Show your ID card and admission ticket at the hall entrance.",
                            seat.getHallNumber(), seat.getSeatNumber());
                } else {
                    return "❌ No allocation found for roll number " + rollNo
                            + ".\n\nPlease ask the admin to run the allocation process or check your roll number.";
                }
            }
        }

        // Time related questions
        if (lowerMessage.contains("when") || lowerMessage.contains("time") || lowerMessage.contains("schedule")
                || lowerMessage.contains("start")) {
            return "⏰ **Exam Schedule:**\n\nPlease check the exam schedule posted on the admin dashboard or contact the examination office. Remember to arrive 15 minutes before the scheduled time.";
        }

        // Rules and regulations
        if (lowerMessage.contains("rule") || lowerMessage.contains("regulation") || lowerMessage.contains("allowed")
                || lowerMessage.contains("not allowed") || lowerMessage.contains("can i")) {
            return "📋 **Exam Rules:**\n\n✓ Arrive early\n✓ Bring admission ticket & ID\n✓ No mobile phones\n✓ No calculators (unless specified)\n✓ Use only blue/black pen\n\nFor detailed rules, contact the examination office.";
        }

        // Greetings
        if (lowerMessage.contains("hello") || lowerMessage.contains("hi") || lowerMessage.contains("hey")) {
            return "👋 Hello! I'm your exam assistant. I can help you with:\n\n• Your seat allocation\n• Exam schedules\n• Exam rules\n• General exam info\n\nWhat would you like to know?";
        }

        // Thanks
        if (lowerMessage.contains("thank") || lowerMessage.contains("thanks")) {
            return "You're welcome! 🎓 Good luck with your exams! If you have more questions, feel free to ask.";
        }

        // How to find
        if (lowerMessage.contains("how") || lowerMessage.contains("where") || lowerMessage.contains("find")) {
            return "💡 **Here's how to use this system:**\n\n1️⃣ Enter your roll number\n2️⃣ Click \"Check Allocation\"\n3️⃣ View your seat details\n4️⃣ Ask me any questions!\n\nWhat would you like help with?";
        }

        // Default helpful response
        return "I'm your exam assistant! 🤖\n\nI can help with:\n• Seat allocations (enter your roll number)\n• Exam schedules & timing\n• Exam rules & procedures\n\nWhat's your question?";
    }

    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
