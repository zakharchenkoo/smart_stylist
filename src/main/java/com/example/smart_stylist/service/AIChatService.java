package com.example.smart_stylist.service;

import com.example.smart_stylist.dto.ChatMessage;
import com.example.smart_stylist.entity.Clothes;
import com.example.smart_stylist.repository.ClothesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIChatService {

//    @Autowired
//    private GeminiService geminiService;

    @Autowired
    private HuggingFaceService huggingFaceService;

    @Autowired
    private ClothesRepository clothesRepository;

    public ChatMessage startConversation() {
        String initialMessage = "Hello! I'm your personal AI fashion stylist. 👗✨\n\n" +
                "I'll help you create the perfect outfit by asking a few questions.\n\n" +
                "Let's start: What's the weather like today?\n" +
                "A) Cold (below 10°C)\n" +
                "B) Cool (10-20°C)\n" +
                "C) Warm (20-25°C)\n" +
                "D) Hot (above 25°C)";

        return new ChatMessage("assistant", initialMessage);
    }

    public ChatMessage continueConversation(Long userId, List<ChatMessage> conversationHistory) throws IOException {
        try {
            List<Clothes> userClothes = clothesRepository.findByUserId(userId);

            if (userClothes.isEmpty()) {
                return new ChatMessage("assistant",
                        "I notice you don't have any clothes in your wardrobe yet. " +
                                "Please add some clothes first, and then I can help you create amazing outfits! 👕👗");
            }

            StringBuilder context = new StringBuilder();
            context.append("You are a professional fashion stylist having a conversation with a client.\n\n");
            context.append("CONVERSATION HISTORY:\n");

            for (ChatMessage msg : conversationHistory) {
                context.append(msg.getRole().toUpperCase()).append(": ").append(msg.getContent()).append("\n");
            }

            context.append("\nAVAILABLE CLOTHES IN WARDROBE:\n");
            context.append(formatClothesForAI(userClothes));

            context.append("\nINSTRUCTIONS:\n");
            context.append("1. If this is the first question (about weather), ask about the occasion next\n");
            context.append("2. If this is the second question (about occasion), ask about style preference next\n");
            context.append("3. If this is the third question (about style), ask about color preference next\n");
            context.append("4. If this is the fourth question (about colors), provide a FINAL RECOMMENDATION\n\n");

            context.append("For the FINAL RECOMMENDATION, respond in this EXACT format:\n");
            context.append("RECOMMENDATION:\n");
            context.append("Here's your perfect outfit:\n\n");
            context.append("👕 [Item name] - [Color]\n");
            context.append("👖 [Item name] - [Color]\n");
            context.append("👟 [Item name] - [Color]\n");
            context.append("👜 [Item name] - [Color] (optional)\n\n");
            context.append("Why this outfit works:\n");
            context.append("[Explanation based on weather, occasion, style, and colors]\n\n");
            context.append("ITEMS:[id1,id2,id3,id4]\n");

            context.append("\nProvide your next question or final recommendation:");

//            String aiResponse = geminiService.getStyleRecommendation(context.toString());

            String aiResponse = huggingFaceService.getStyleRecommendation(context.toString());

            System.out.println("=== AI CHAT RESPONSE ===");
            System.out.println(aiResponse);
            System.out.println("========================");

            return new ChatMessage("assistant", aiResponse);

        } catch (IOException e) {
            if (e.getMessage().contains("429")) {
                return new ChatMessage("assistant",
                        "⏰ I'm receiving too many requests right now. " +
                                "Please wait a minute and try again. " +
                                "The free API has a limit of 60 requests per minute. 😊");
            }
            throw e;
        }
    }

    private String formatClothesForAI(List<Clothes> clothes) {
        StringBuilder sb = new StringBuilder();

        sb.append("BASE LAYER:\n");
        clothes.stream()
                .filter(c -> c.getCategory().equals("SHIRTS_BLOUSES") || c.getCategory().equals("TSHIRTS_TOPS"))
                .forEach(c -> sb.append("- ID:").append(c.getId()).append(" ")
                        .append(c.getName()).append(" (").append(c.getColor()).append(")\n"));

        sb.append("\nOUTER LAYER:\n");
        clothes.stream()
                .filter(c -> c.getCategory().equals("COATS_JACKETS") ||
                        c.getCategory().equals("SWEATERS") ||
                        c.getCategory().equals("BLAZERS_VESTS") ||
                        c.getCategory().equals("SUITS"))
                .forEach(c -> sb.append("- ID:").append(c.getId()).append(" ")
                        .append(c.getName()).append(" (").append(c.getColor()).append(")\n"));

        sb.append("\nBOTTOMS:\n");
        clothes.stream()
                .filter(c -> c.getCategory().equals("JEANS_TROUSERS") ||
                        c.getCategory().equals("SHORTS") ||
                        c.getCategory().equals("SKIRTS"))
                .forEach(c -> sb.append("- ID:").append(c.getId()).append(" ")
                        .append(c.getName()).append(" (").append(c.getColor()).append(")\n"));

        sb.append("\nDRESSES:\n");
        clothes.stream()
                .filter(c -> c.getCategory().equals("DRESSES_JUMPSUITS"))
                .forEach(c -> sb.append("- ID:").append(c.getId()).append(" ")
                        .append(c.getName()).append(" (").append(c.getColor()).append(")\n"));

        sb.append("\nSHOES:\n");
        clothes.stream()
                .filter(c -> c.getCategory().equals("SHOES"))
                .forEach(c -> sb.append("- ID:").append(c.getId()).append(" ")
                        .append(c.getName()).append(" (").append(c.getColor()).append(")\n"));

        sb.append("\nACCESSORIES:\n");
        clothes.stream()
                .filter(c -> c.getCategory().equals("BAGS_ACCESSORIES") ||
                        c.getCategory().equals("HATS_SCARVES"))
                .forEach(c -> sb.append("- ID:").append(c.getId()).append(" ")
                        .append(c.getName()).append(" (").append(c.getColor()).append(")\n"));

        return sb.toString();
    }

    public List<Long> extractClothesIds(String recommendation) {
        try {
            int startIndex = recommendation.indexOf("ITEMS:[");
            if (startIndex == -1) return List.of();

            int endIndex = recommendation.indexOf("]", startIndex);
            if (endIndex == -1) return List.of();

            String idsString = recommendation.substring(startIndex + 7, endIndex);

            return List.of(idsString.split(",")).stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Failed to extract clothes IDs: " + e.getMessage());
            return List.of();
        }
    }
}
