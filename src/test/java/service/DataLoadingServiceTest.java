package service;

import com.lucasnscr.ai_financial_analyst.model.CryptoEnum;
import com.lucasnscr.ai_financial_analyst.model.StockEnum;
import com.lucasnscr.ai_financial_analyst.service.DataLoadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class DataLoadingServiceTest {


    @InjectMocks
    private DataLoadingService dataLoadingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadData() {
        doNothing().when(dataLoadingService).loadEntities(any(), any());
        dataLoadingService.loadData();
        verify(dataLoadingService, times(1)).loadEntities(eq(StockEnum.values()), any());
        verify(dataLoadingService, times(1)).loadEntities(eq(CryptoEnum.values()), any());
    }
}