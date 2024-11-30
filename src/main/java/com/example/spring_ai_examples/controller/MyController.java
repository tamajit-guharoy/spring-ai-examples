package com.example.spring_ai_examples.controller;

import com.example.spring_ai_examples.service.MockWeatherService;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.model.Media;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
public class MyController {

    record Request(String question) {
    }

    @Autowired
    private final OllamaChatModel ollamaChatModel = null;
    @Autowired
    private final OpenAiChatModel openAiChatModel = null;

    @Autowired
    MockWeatherService currentWeather = null;

    @Autowired
    private final OllamaEmbeddingModel ollamaEmbeddingModel=null;



//    public MyController(OllamaChatModel ollamaChatModel, OpenAiChatModel openAiChatModel) {
//
//    }

    @PostMapping("/ollama")
    public ChatResponse ollama(@RequestBody Request request) {
        ChatResponse chatResponse = ollamaChatModel.call(new Prompt(request.question));
        return chatResponse;
    }

    @PostMapping("/openai")
    public ChatResponse openai(@RequestBody Request request) {
        ChatResponse chatResponse = openAiChatModel.call(new Prompt(request.question));
        return chatResponse;
    }


    @PostMapping("/ollama/image")
    public ChatResponse ollamaImage(@RequestBody Request request) throws IOException {

        Resource imageResource = new ClassPathResource("download.jpg");

        InputStream inputStream = imageResource.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);

        if (image != null) {
            System.out.println("Image loaded successfully!");
            // You can now work with the image (e.g., display, manipulate, etc.)
        } else {
            System.out.println("Failed to load the image.");
        }

        var userMessage = new UserMessage("Explain what do you see on this picture?",
                new Media(MimeTypeUtils.IMAGE_JPEG, imageResource));

        ChatResponse response = ollamaChatModel.call(new Prompt(userMessage,
                OllamaOptions.builder().withModel(OllamaModel.LLAVA)));
        return response;
    }

    @PostMapping("/ollama/function")
    public ChatResponse ollamaFunction(@RequestBody Request request) {

        UserMessage userMessage = new UserMessage(request.question);

        var promptOptions = OllamaOptions.builder()
                .withFunctionCallbacks(List.of(FunctionCallback.builder()
                            //    .description("")
                        .function("CurrentWeather", currentWeather) // (1) function name and instance
                        .inputType(MockWeatherService.Request.class) // (3) function signature
                        .build())) // function code
                .build();

        ChatResponse chatResponse = this.ollamaChatModel.call(new Prompt(userMessage, promptOptions));
        return chatResponse;
    }


    @PostMapping("/ollama/embedding")
    public Map ollamaEmbedding(@RequestBody Request request) {

        EmbeddingResponse embeddingResponse;
        embeddingResponse = ollamaEmbeddingModel.call(
                new EmbeddingRequest(List.of(request.question),
                        OllamaOptions.builder()
                                .withModel("llama3.2")
                        .withTruncate(false).build()));
        ; return Map.of("embedding", embeddingResponse);
    }
}
