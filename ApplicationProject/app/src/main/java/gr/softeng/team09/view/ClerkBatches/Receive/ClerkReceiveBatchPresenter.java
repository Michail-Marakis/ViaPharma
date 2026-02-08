package gr.softeng.team09.view.ClerkBatches.Receive;

import gr.softeng.team09.domain.ProductCategory;

/**
 * The type Clerk receive batch presenter.
 */
public class ClerkReceiveBatchPresenter {

    private ClerkReceiveBatchView view;
    private int EofInput = -1;
    private String ProductName;
    private int numberOfBatches = -1;
    private double priceProduct = -1.0;
    private ProductCategory productCategory = null;
    private int ProductsEachBatch = -1;

    /**
     * Set view.
     *
     * @param view the view
     */
    public void setView(ClerkReceiveBatchView view){
        this.view = view;
    }

    /**
     * Set eof input.
     *
     * @param eof the eof
     */
//calling them on all inputs
    public void setEofInput(int eof){
        EofInput = eof;
    }

    /**
     * Set product name.
     *
     * @param name the name
     */
    public void setProductName(String name){
        ProductName = name;
    }

    /**
     * Set number of batches.
     *
     * @param numberOfBatchesInput the number of batches input
     */
    public void setNumberOfBatches(int numberOfBatchesInput){
        numberOfBatches = numberOfBatchesInput;
    }

    /**
     * Set price product.
     *
     * @param priceInput the price input
     */
    public void setPriceProduct(double priceInput){
        priceProduct = priceInput;
    }

    /**
     * Set product category.
     *
     * @param pr the pr
     */
    public void setProductCategory(ProductCategory pr){
        productCategory = pr;
    }

    /**
     * Set product each batch.
     *
     * @param numberOfProductsEachBatchInput the number of products each batch input
     */
    public void setProductEachBatch(int numberOfProductsEachBatchInput){
        ProductsEachBatch = numberOfProductsEachBatchInput;
    }

    /**
     * Ready to receive.
     */
    public void ReadyToReceive(){
        if(EofInput == -1 || ProductName == null || numberOfBatches == -1 || priceProduct == -1.0 || productCategory == null || ProductsEachBatch == -1){
            view.showError("Fill all fields!");
            return;
        }
        view.receiveBatchCall(EofInput, ProductName ,ProductsEachBatch, numberOfBatches, priceProduct,productCategory);
        ResetValues();
        view.showSuccess("Stock updated successfully!");
    }

    private void ResetValues(){
        this.EofInput = -1;
        this.ProductName = null;
        this.numberOfBatches = -1;
        this.priceProduct = -1.0;
        this.productCategory = null;
        this.ProductsEachBatch = -1;
    }
}
