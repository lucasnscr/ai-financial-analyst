//package converter;
//
//import com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals.NewsAndSentimentalsCryptoConverter;
//import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
//import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
//import org.json.JSONException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Mockito;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//
//public class CryptoConverterTest {
//
//    @Mock
//    private LLMContent llmContent;
//
//    @InjectMocks
//    private NewsAndSentimentalsCryptoConverter cryptoConverter;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testConvertJsonToCrypto() throws JSONException {
//        // Arrange
//        String cryptoName = "Bitcoin";
//        JSONObject jsonResponse = new JSONObject();
//        JSONArray feedArray = new JSONArray();
//        JSONObject feedItem = new JSONObject();
//        feedItem.put("content", "Sample Content");
//        feedArray.put(feedItem);
//        jsonResponse.put("feed", feedArray);
//
//        Mockito.when(llmContent.prepareLLMContent(anyString(), eq(feedArray), eq(0)))
//                .thenReturn("Processed Content");
//
//        // Act
//        CryptoNewsAndSentimentals result = cryptoConverter.convertJsonToCrypto(cryptoName, jsonResponse);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(cryptoName, result.getName());
//
//        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        assertEquals(expectedDate, result.getDate());
//
//        List<String> contentForLLM = result.getContentforLLM();
//        assertNotNull(contentForLLM);
//        assertEquals(1, contentForLLM.size());
//        assertEquals("Processed Content", contentForLLM.get(0));
//
//        // Verify that the mock was called as expected
//        Mockito.verify(llmContent, Mockito.times(1))
//                .prepareLLMContent(anyString(), eq(feedArray), eq(0));
//    }
//}