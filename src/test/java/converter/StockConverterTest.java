//package converter;
//
//import com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals.NewsAndSentimentalsStockConverter;
//import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
//import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Mockito;
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
//public class StockConverterTest {
//
//    @Mock
//    private LLMContent llmContent;
//
//    @InjectMocks
//    private NewsAndSentimentalsStockConverter stockConverter;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testConvertJsonToStock() throws JSONException {
//        String stockName = "Apple";
//        JSONObject jsonResponse = new JSONObject();
//        JSONArray feedArray = new JSONArray();
//        JSONObject feedItem = new JSONObject();
//        feedItem.put("content", "Sample StockNewsAndSentimentals Content");
//        feedArray.put(feedItem);
//        jsonResponse.put("feed", feedArray);
//
//        Mockito.when(llmContent.prepareLLMContent(anyString(), eq(feedArray), eq(0)))
//                .thenReturn("Processed StockNewsAndSentimentals Content");
//
//        StockNewsAndSentimentals result = stockConverter.convertJsonToStock(stockName, jsonResponse);
//
//        assertNotNull(result);
//        assertEquals(stockName, result.getName());
//
//        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        assertEquals(expectedDate, result.getDate());
//
//        List<String> contentForLLM = result.getContentforLLM();
//        assertNotNull(contentForLLM);
//        assertEquals(1, contentForLLM.size());
//        assertEquals("Processed StockNewsAndSentimentals Content", contentForLLM.get(0));
//
//        Mockito.verify(llmContent, Mockito.times(1))
//                .prepareLLMContent(anyString(), eq(feedArray), eq(0));
//    }
//
////    @Test
//    public void testConvertJsonToStockWithEmptyFeed() throws JSONException {
//        String stockName = "Tesla";
//        JSONObject jsonResponse = new JSONObject();
//        jsonResponse.put("feed", new JSONArray());
//
//        Mockito.when(llmContent.prepareLLMContent(anyString(), Mockito.any(), Mockito.anyInt()))
//                .thenReturn(null);
//
//        StockNewsAndSentimentals result = stockConverter.convertJsonToStock(stockName, jsonResponse);
//
//        assertNotNull(result);
//        assertEquals(stockName, result.getName());
//
//        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        assertEquals(expectedDate, result.getDate());
//
//        List<String> contentForLLM = result.getContentforLLM();
//        assertNotNull(contentForLLM);
//        assertEquals(1, contentForLLM.size());
//        assertEquals(null, contentForLLM.get(0));
//
//        Mockito.verify(llmContent, Mockito.times(1))
//                .prepareLLMContent(anyString(), Mockito.any(), Mockito.anyInt());
//    }
//}
