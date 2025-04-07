import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

// This class wraps around the OpenAI API and gives you cool utilities to summarize,
// analyze, and extract related topics from news articles using GPT.
public class OpenAIService {
  private final OpenAiService service;
  private static final String MODEL = "gpt-3.5-turbo";

  // When you create an instance of this class, you pass in your API key.
  // The OpenAiService gets set up with a 60-second timeout.
  public OpenAIService(String apiKey) {
    this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
  }




  /**
   * Summarizes article content using OpenAI.
   * 
   * @param content The full article text you want to summarize
   * @return A short summary (around 30 words)
   */
  public String summarizeArticle(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // First, tell the model what kind of assistant it is
    messages.add(new ChatMessage("system",
        "You are a helpful assistant that summarizes news articles concisely."));

    // Then, ask it to summarize the specific content in user-friendly language
    messages.add(new ChatMessage("user",
        "Please summarize the following news article in 30 words: " + content));

    // Set up the request with model settings like max tokens and creativity
    // (temperature)
    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(150) // Enough tokens for a 30-word summary
        .temperature(0.7) // A bit of creativity, but not too much
        .build();

    // Send the request to OpenAI and get the response
    ChatCompletionResult result = service.createChatCompletion(request);

    // Return just the text content from the model's first response
    return result.getChoices().get(0).getMessage().getContent();
  }

  /**
   * Analyzes sentiment of a news article using OpenAI.
   * 
   * @param content The full article text
   * @return A simple sentiment: "positive", "negative", or "neutral"
   */
  public String analyzeSentiment(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // Set the behavior: the model is now a sentiment analyzer
    messages.add(new ChatMessage("system",
        "You are a sentiment analysis tool for news articles."));

    // Ask the model to tell us the sentiment with just one word
    messages.add(new ChatMessage("user",
        "Analyze the sentiment of this news article and respond with only one word: positive, negative, or neutral: "
            + content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(10) // We only expect one word in return
        .temperature(0.3) // Less creativity, more accuracy
        .build();

    ChatCompletionResult result = service.createChatCompletion(request);

    // Trim in case it returns something like " neutral " with extra whitespace
    return result.getChoices().get(0).getMessage().getContent().trim();
  }

  /**
   * Suggests related keywords or topics for the article.
   * 
   * @param content The article to analyze
   * @return A string containing 5 related keywords or topics
   */
  public String suggestRelatedTopics(String content) {
    List<ChatMessage> messages = new ArrayList<>();

    // Set up the model to behave like a keyword generator
    messages.add(new ChatMessage("system",
        "You are a tool that suggests related topics and keywords for news articles."));

    // Ask it to generate 5 topics based on the article
    messages.add(new ChatMessage("user",
        "Based on this news article, suggest 5 related topics or keywords: " + content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(MODEL)
        .messages(messages)
        .maxTokens(100) // Enough space for 5 keywords or short sentences
        .temperature(0.7) // Allow some creativity in picking the related topics
        .build();

    ChatCompletionResult result = service.createChatCompletion(request);

    return result.getChoices().get(0).getMessage().getContent();
  }
}
