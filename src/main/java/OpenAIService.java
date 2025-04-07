import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OpenAIService {
  private final OpenAiService service;
  private static final String MODEL = "gpt-3.5-turbo";

  public OpenAIService(String apiKey) {
    // Initialize the service with a timeout of 60 seconds
    this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
  }

  /**
   * Summarizes article content using OpenAI
   * 
   * @param content The article content to summarize
   * @return A summarized version of the content
   */
  public String summarizeArticle(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // Add system message to set context
    messages.add(new ChatMessage("system",
        "You are a helpful assistant that summarizes news articles concisely."));

    // Add user message with the content to summarize
    messages.add(new ChatMessage("user",
        "Please summarize the following news article in 30 words: " + content));

    // Create the completion request
    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(150)
        .temperature(0.7)
        .build();

    // Execute the request and get the result
    ChatCompletionResult result = service.createChatCompletion(request);

    // Return the content of the response
    return result.getChoices().get(0).getMessage().getContent();
  }

  /**
   * Analyzes news sentiment using OpenAI
   * 
   * @param content The article content to analyze
   * @return A sentiment analysis result (positive, negative, or neutral)
   */
  public String analyzeSentiment(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    messages.add(new ChatMessage("system",
        "You are a sentiment analysis tool for news articles."));

    messages.add(new ChatMessage("user",
        "Analyze the sentiment of this news article and respond with only one word: positive, negative, or neutral: "
            + content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(10)
        .temperature(0.3)
        .build();

    ChatCompletionResult result = service.createChatCompletion(request);

    return result.getChoices().get(0).getMessage().getContent().trim();
  }

  /**
   * Suggests related topics based on article content
   * 
   * @param content The article content to analyze
   * @return A list of related topics/keywords
   */
  public String suggestRelatedTopics(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    messages.add(new ChatMessage("system",
        "You are a tool that suggests related topics and keywords for news articles."));

    messages.add(new ChatMessage("user",
        "Based on this news article, suggest 5 related topics or keywords: " + content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(100)
        .temperature(0.7)
        .build();

    ChatCompletionResult result = service.createChatCompletion(request);

    return result.getChoices().get(0).getMessage().getContent();
  }
}