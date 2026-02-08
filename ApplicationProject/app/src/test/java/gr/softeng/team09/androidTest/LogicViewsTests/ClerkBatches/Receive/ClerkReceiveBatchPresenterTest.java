package gr.softeng.team09.androidTest.LogicViewsTests.ClerkBatches.Receive;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.androidTest.LogicViewsTests.PresenterTestBase;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.view.ClerkBatches.Receive.ClerkReceiveBatchPresenter;
import gr.softeng.team09.view.ClerkBatches.Receive.ClerkReceiveBatchView;

import static org.junit.Assert.*;

/**
 * The type Clerk receive batch presenter test.
 */
public class ClerkReceiveBatchPresenterTest extends PresenterTestBase {

    private ClerkReceiveBatchPresenter presenter;
    private FakeView view;

    private static class FakeView implements ClerkReceiveBatchView {
        /**
         * The Last error.
         */
        String lastError = null;
        /**
         * The Last success.
         */
        String lastSuccess = null;

        /**
         * The Receive batch call count.
         */
        int receiveBatchCallCount = 0;
        /**
         * The Last eof.
         */
        int lastEof;
        /**
         * The Last product name.
         */
        String lastProductName;
        /**
         * The Last n products.
         */
        int lastNProducts;
        /**
         * The Last n batches.
         */
        int lastNBatches;
        /**
         * The Last price.
         */
        double lastPrice;
        /**
         * The Last category.
         */
        ProductCategory lastCategory;

        @Override
        public int generateBatchNumber() {
            return 0;
        }

        @Override
        public void showError(String msg) { lastError = msg; }

        @Override
        public void receiveBatchCall(int eof, String productName, int nProducts, int nBatches,
                                     double price, ProductCategory productCategory) {
            receiveBatchCallCount++;
            lastEof = eof;
            lastProductName = productName;
            lastNProducts = nProducts;
            lastNBatches = nBatches;
            lastPrice = price;
            lastCategory = productCategory;
        }

        @Override
        public void showSuccess(String msg) { lastSuccess = msg; }

        /**
         * Reset messages.
         */
        void resetMessages() {
            lastError = null;
            lastSuccess = null;
        }
    }

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        view = new FakeView();
        presenter = new ClerkReceiveBatchPresenter();
        presenter.setView(view);
    }

    /**
     * Ready to receive flow covers error then success then reset.
     */
    @Test
    public void readyToReceive_flow_coversErrorThenSuccessThenReset() {
        // 1) Missing fields -> error branch
        presenter.ReadyToReceive();
        assertEquals("Fill all fields!", view.lastError);
        assertNull(view.lastSuccess);
        assertEquals(0, view.receiveBatchCallCount);

        // 2) Fill all fields -> success branch + receiveBatchCall
        ProductCategory anyCategory = ProductCategory.values()[0];

        presenter.setEofInput(123);
        presenter.setProductName("Panadol");
        presenter.setProductEachBatch(10);
        presenter.setNumberOfBatches(2);
        presenter.setPriceProduct(4.5);
        presenter.setProductCategory(anyCategory);

        view.resetMessages();
        presenter.ReadyToReceive();

        assertNull(view.lastError);
        assertEquals("Stock updated successfully!", view.lastSuccess);
        assertEquals(1, view.receiveBatchCallCount);

        assertEquals(123, view.lastEof);
        assertEquals("Panadol", view.lastProductName);
        assertEquals(10, view.lastNProducts);
        assertEquals(2, view.lastNBatches);
        assertEquals(4.5, view.lastPrice, 0.0000001);
        assertEquals(anyCategory, view.lastCategory);

        // 3) After ResetValues(), fields έγιναν default -> ξανά error branch
        view.resetMessages();
        presenter.ReadyToReceive();

        assertEquals("Fill all fields!", view.lastError);
        assertNull(view.lastSuccess);
        assertEquals(1, view.receiveBatchCallCount); // δεν αυξήθηκε
    }
}
