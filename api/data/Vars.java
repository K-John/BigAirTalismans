package scripts.api.data;

import lombok.Getter;
import org.tribot.script.sdk.Inventory;
import scripts.TaskSet;
import scripts.api.classes.Talisman;
import scripts.api.tasks.DialogueTask;
import scripts.api.tasks.TalismanTask;
import scripts.api.tasks.BankTask;
import scripts.api.tasks.PickupTask;

@Getter
public class Vars {
    private static final Vars instance = new Vars();
    private final TaskSet tasks = new TaskSet(new BankTask(), new DialogueTask(), new PickupTask(), new TalismanTask());
    private boolean isRunning = true;
    private String status = null;
    private boolean debug = false;
    private boolean bankClear = false;
    private int startingCount = -1;

    public static Vars get() {
        return instance;
    }

    public void stopRunning() {
        this.isRunning = false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setBankClear(boolean bankClear) {
        this.bankClear = bankClear;
    }

    public void setStartingCount(int startingCount) {
        this.startingCount = startingCount;
    }

    public int getCollectedCount() {
        if (startingCount == -1) { return 0; }
        return Talisman.droppedCount() + Inventory.getCount(Constants.NOTED_AIR_TALISMAN, Constants.AIR_TALISMAN) - startingCount;
    }
}