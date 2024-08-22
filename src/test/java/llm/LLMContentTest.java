//package llm;
//
//import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class LLMContentTest {
//
//    private LLMContent llmContent;
//
//    @BeforeEach
//    public void setUp() {
//        llmContent = new LLMContent();
//    }
//
//    @Test
//    public void testPrepareLLMContent() throws JSONException {
//        // Arrange
//        String name = "Apple";
//        String title = "Apple Launches New Product";
//        String url = "https://example.com/apple-launch";
//        String timePublished = "20230815T103000";
//        String summary = "Apple has launched a new product...";
//        double overallSentimentScore = 0.85;
//        String overallSentimentLabel = "Positive";
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("title", title);
//        jsonObject.put("url", url);
//        jsonObject.put("time_published", timePublished);
//        jsonObject.put("summary", summary);
//        jsonObject.put("overall_sentiment_score", overallSentimentScore);
//        jsonObject.put("overall_sentiment_label", overallSentimentLabel);
//
//        JSONArray array = new JSONArray();
//        array.put(jsonObject);
//
//        // Act
//        String result = llmContent.prepareLLMContent(name, array, 0);
//
//        // Assert
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
//        LocalDateTime dateTime = LocalDateTime.parse(timePublished, formatter);
//
//        String expectedOutput = String.format("""
//                        Article Information about: %s
//                        Title: %s
//                        url: %s
//                        Time: %s
//                        Summary: %s
//                        Overall Sentiment Score: %.2f
//                        Overall Sentiment Label: %s""",
//                name, title, url, dateTime, summary, overallSentimentScore, overallSentimentLabel);
//
//        assertEquals(expectedOutput, result);
//    }
//
//    @Test
//    public void testPrepareLLMContentWithMultipleArticles() throws JSONException {
//        // Arrange
//        String name = "Tesla";
//        String title1 = "Tesla Stock Surges";
//        String url1 = "https://example.com/tesla-stock-surges";
//        String timePublished1 = "20230816T114500";
//        String summary1 = "Tesla's stock price increased significantly...";
//        double overallSentimentScore1 = 0.92;
//        String overallSentimentLabel1 = "Very Positive";
//
//        String title2 = "Tesla Faces Challenges";
//        String url2 = "https://example.com/tesla-challenges";
//        String timePublished2 = "20230817T090000";
//        String summary2 = "Tesla faces new challenges in the market...";
//        double overallSentimentScore2 = -0.40;
//        String overallSentimentLabel2 = "Negative";
//
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("title", title1);
//        jsonObject1.put("url", url1);
//        jsonObject1.put("time_published", timePublished1);
//        jsonObject1.put("summary", summary1);
//        jsonObject1.put("overall_sentiment_score", overallSentimentScore1);
//        jsonObject1.put("overall_sentiment_label", overallSentimentLabel1);
//
//        JSONObject jsonObject2 = new JSONObject();
//        jsonObject2.put("title", title2);
//        jsonObject2.put("url", url2);
//        jsonObject2.put("time_published", timePublished2);
//        jsonObject2.put("summary", summary2);
//        jsonObject2.put("overall_sentiment_score", overallSentimentScore2);
//        jsonObject2.put("overall_sentiment_label", overallSentimentLabel2);
//
//        JSONArray array = new JSONArray();
//        array.put(jsonObject1);
//        array.put(jsonObject2);
//
//        // Act
//        String result1 = llmContent.prepareLLMContent(name, array, 0);
//        String result2 = llmContent.prepareLLMContent(name, array, 1);
//
//        // Assert
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
//        LocalDateTime dateTime1 = LocalDateTime.parse(timePublished1, formatter);
//        LocalDateTime dateTime2 = LocalDateTime.parse(timePublished2, formatter);
//
//        String expectedOutput1 = String.format("""
//                        Article Information about: %s
//                        Title: %s
//                        url: %s
//                        Time: %s
//                        Summary: %s
//                        Overall Sentiment Score: %.2f
//                        Overall Sentiment Label: %s""",
//                name, title1, url1, dateTime1, summary1, overallSentimentScore1, overallSentimentLabel1);
//
//        String expectedOutput2 = String.format("""
//                        Article Information about: %s
//                        Title: %s
//                        url: %s
//                        Time: %s
//                        Summary: %s
//                        Overall Sentiment Score: %.2f
//                        Overall Sentiment Label: %s""",
//                name, title2, url2, dateTime2, summary2, overallSentimentScore2, overallSentimentLabel2);
//
//        assertEquals(expectedOutput1, result1);
//        assertEquals(expectedOutput2, result2);
//    }
//}