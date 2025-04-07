import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// A simple helper class to talk to OpenAI and get cool stuff like summaries, sentiment, and related topics from news articles
public class OpenAIService {
  private final OpenAiService service;
  private static final String MODEL = "gpt-3.5-turbo"; // Yep, we're using ChatGPT under the hood

  // When you create this service, just pass your OpenAI API key — we'll handle
  // the rest
  public OpenAIService(String apiKey) {
    // Connect to OpenAI with a timeout so things don’t hang forever
    this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
  }




  /**
   * Want a quick summary of a long article? This will do it in ~30 words.
   */
  public String summarizeArticle(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // First, tell the AI what kind of assistant we want it to be
    messages.add(new ChatMessage("system",
        "You are a helpful assistant that summarizes news articles concisely."));

    // Now feed it the actual article and ask for a summary
    messages.add(new ChatMessage("user",
        "Please summarize the following news article in 30 words: " + content));

    // Build the chat request with the model + how creative we want it to be
    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(150) // That’s enough for about 30–40 words
        .temperature(0.7) // Medium creativity
        .build();

    // Send the request and get the AI’s response
    ChatCompletionResult result = service.createChatCompletion(request);

    // Grab and return just the response text (we only care about the first one)
    return result.getChoices().get(0).getMessage().getContent();
  }

  /**
   * Need to know how a news article "feels"? This tells you if it's positive,
   * negative, or neutral — in one word.
   */
  public String analyzeSentiment(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // Again, let the AI know what role it's playing
    messages.add(new ChatMessage("system",
        "You are a sentiment analysis tool for news articles."));

    // Ask it to be short and sweet — just one word
    messages.add(new ChatMessage("user",
        "Analyze the sentiment of this news article and respond with only one word: positive, negative, or neutral: "
            + content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(10) // Super short — one word response
        .temperature(0.3) // Stay accurate, not creative here
        .build();

    ChatCompletionResult result = service.createChatCompletion(request);

    // Clean it up in case there's extra whitespace
    return result.getChoices().get(0).getMessage().getContent().trim();
  }

  /**
   * This one gives you some related topics or keywords for an article — great for
   * tagging or recommendations.
   */
  public String suggestRelatedTopics(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // Set the scene again — this time the AI is a topic suggester
    messages.add(new ChatMessage("system",
        "You are a tool that suggests related topics and keywords for news articles."));

    // Ask for 5 related topics/keywords — nice and straightforward
    messages.add(new ChatMessage("user",
        "Based on this news article, suggest 5 related topics or keywords: " + content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(100) // Leave some room for a short list
        .temperature(0.7) // Let the AI be a bit creative here
        .build();

    ChatCompletionResult result = service.createChatCompletion(request);

    return result.getChoices().get(0).getMessage().getContent();
  }
}
