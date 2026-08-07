package interface_adapter.news;

import org.junit.jupiter.api.Test;
import use_case.news.NewsInputBoundary;
import use_case.news.NewsInputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NewsControllerTest {

    @Test
    void testExecutePassesTickerToInputBoundary() {
        final TestNewsInputBoundary inputBoundary =
                new TestNewsInputBoundary();
        final NewsController controller = new NewsController(inputBoundary);

        controller.execute("AAPL");

        assertNotNull(inputBoundary.inputData);
        assertEquals("AAPL", inputBoundary.inputData.getTicker());
    }

    private static final class TestNewsInputBoundary
            implements NewsInputBoundary {

        private NewsInputData inputData;

        @Override
        public void execute(NewsInputData newsInputData) {
            inputData = newsInputData;
        }
    }
}
