package gr.softeng.team09.view.ClerkBatches.Recall;

import androidx.annotation.NonNull;

import java.util.List;

import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk recall batch presenter.
 */
public class ClerkRecallBatchPresenter {

    private ClerkRecallBatchView view;
    private final Inventory inventory = MemoryStore.getInventory();

    private int idToRecall = -1;

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkRecallBatchView view) {
        this.view = view;
    }

    /**
     * Sets id to recall.
     *
     * @param id the id
     */
    public void setIdToRecall(int id) {
        this.idToRecall = id;
    }

    /**
     * Ready to recall.
     */
    public void readyToRecall() {
        if (idToRecall <= 0) {
            view.showError("Give batch id");
            return;
        }

        Batch found = null;
        List<Batch> batches = inventory.getAllBatches();
        for (Batch b : batches) {
            if (b != null && b.getId() == idToRecall) {
                found = b;
                break;
            }
        }

        if (found == null) {
            view.showError("Batch not found");
            idToRecall = -1;
            return;
        }

        view.recallBatchCall(idToRecall);
        idToRecall = -1;
        view.showSuccess("Batch recalled successfully");
        for(int i = 0; i<found.getDistributedTo().size(); i++){
            StringBuilder strBuild = getStringBuilder(found, i);
            inventory.getOwner().setEmailsSent(strBuild, found.getDistributedTo().get(i).getOwner());
            view.showSuccess("Email sent to "+found.getDistributedTo().get(i).getOwner().getEmail());
        }
    }

    @NonNull
    private StringBuilder getStringBuilder(Batch found, int i) {
        StringBuilder strBuild = new StringBuilder();
        String Header = "Email sent successfully!\n";
        strBuild.append(Header);
        String value = "Sent to: " + found.getDistributedTo().get(i).getOwner().getEmail() + "\n\n";
        strBuild.append(value);
        String Text = "Context: ATTENTION, PLEASE SEND US BACK THE PRODUCTS WITH EOF CODE: " + found.getProduct().getEofyCode();
        strBuild.append(Text);
        String end = "\n\nTHANKS!\nKIND REGARDS" + "\nNAME: "+inventory.getOwner().getName() + "\nPHONE NUMBER: " + inventory.getOwner().getPhone() + "\nEMAIL: " + inventory.getOwner().getEmail();
        strBuild.append(end);
        return strBuild;
    }

}
