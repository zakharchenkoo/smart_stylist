package com.example.smart_stylist.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class HuggingFaceService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.text.model}")
    private String modelName;

    @Value("${huggingface.api.router}")
    private String hfRouterUrl;

    @Value("${huggingface.http.connectTimeout:60}")
    private long connectTimeout;

    @Value("${huggingface.http.readTimeout:60}")
    private long readTimeout;

    @Value("${huggingface.http.writeTimeout:60}")
    private long writeTimeout;

    @Value("${huggingface.retry.delay:3000}")
    private long retryDelay;

    @Value("${huggingface.retry.maxAttempts:3}")
    private int maxAttempts;

    private OkHttpClient client;

    private void initClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .build();
        }
    }

    public String getStyleRecommendation(String prompt) throws IOException {
        initClient();

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", modelName);

        JsonArray messages = new JsonArray();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a helpful AI stylist assistant.");
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        int attempts = 0;
        while (attempts < maxAttempts) {
            attempts++;
            Request request = new Request.Builder()
                    .url(hfRouterUrl)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    if (response.code() == 503) {
                        Thread.sleep(retryDelay);
                        continue; // повторим запрос
                    }
                    switch (response.code()) {
                        case 401 -> throw new IOException("🔑 Invalid API token.");
                        case 403 -> throw new IOException("⛔ Permission denied. Ensure token has 'Make calls to Inference Providers'.");
                        case 429 -> throw new IOException("🚦 Rate limit exceeded.");
                        case 404 -> throw new IOException("❌ Router or model not found.");
                        default -> throw new IOException("HF Router API error " + response.code() + ": " + responseBody);
                    }
                }

                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray choices = json.getAsJsonArray("choices");

                if (choices != null && choices.size() > 0) {
                    JsonObject first = choices.get(0).getAsJsonObject();
                    JsonObject messageObj = first.getAsJsonObject("message");
                    if (messageObj != null && messageObj.has("content")) {
                        return messageObj.get("content").getAsString();
                    }
                }

                throw new IOException("❌ Unexpected HF Router API response: " + responseBody);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Retry interrupted", e);
            }
        }

        throw new IOException("❌ HF Router API failed after " + maxAttempts + " attempts.");
    }
}
