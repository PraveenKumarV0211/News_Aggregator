# News Aggregator with OpenAI Integration

A Java-based news aggregation application that enhances article viewing with AI-powered features such as summarization, sentiment analysis, and related topic suggestions.

## Features

- Browse and search news articles by keywords
- Sort articles by date or popularity
- View top trending articles
- AI-enhanced article viewing with:
  - Article summarization (powered by OpenAI)
  - Sentiment analysis (powered by OpenAI)
  - Related topic suggestions (powered by OpenAI)

## Setup and Configuration

### Prerequisites

- Java 11 or higher
- Maven
- OpenAI API key

### Getting Started

1. **Clone the repository**
   ```
   git clone <repository-url>
   cd news-aggregator
   ```

2. **Configure your OpenAI API key**

   You have three options:
   
   - Set environment variable:
     ```
     export OPENAI_API_KEY=your-api-key-here
     ```
   
   - Edit the `config.properties` file:
     ```
     openai.api.key=your-api-key-here
     ```
   
   - Modify the DEFAULT_API_KEY constant in `NewsAggregatorApp.java`

3. **Build the project**
   ```
   mvn clean package
   ```

4. **Run the application**
   ```
   java -jar target/news-aggregator-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

## Usage

1. Launch the application
2. Use the search bar to find articles by keyword
3. Click the "View" button on an article to open the enhanced AI view
4. Navigate between tabs to see different AI-generated content:
   - Original article
   - AI Summary
   - Sentiment Analysis
   - Related Topics

## Implementation Details

The application uses:
- Custom data structures for managing articles and keywords
- OpenAI's API for natural language processing
- Java Swing for the user interface
- Multi-threading for non-blocking API calls

## Extending the Application

To add new AI features:
1. Implement new methods in the `OpenAIService` class
2. Add corresponding methods in `NewsAggregatorApp`
3. Update the UI to display the new features

## Troubleshooting

- **OpenAI service not available**: Check your API key and internet connection
- **Slow responses**: Consider adjusting the token limits in `config.properties`
- **Build issues**: Ensure you have Maven installed and the pom.xml file is correctly configured

## License

This project is licensed under the MIT License - see the LICENSE file for details.