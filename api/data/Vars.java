package scripts.api.data;

import lombok.Getter;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GroundItem;
import scripts.TaskSet;
import scripts.api.tasks.DialogueTask;
import scripts.api.tasks.TalismanTask;
import scripts.api.tasks.BankTask;
import scripts.api.tasks.PickupTask;

import java.time.Duration;
import java.time.Instant;

@Getter
public class Vars {
    private static final Vars instance = new Vars();
    private final TaskSet tasks = new TaskSet(new BankTask(), new PickupTask(), new TalismanTask());
    private final TaskSet tasks = new TaskSet(new BankTask(), new DialogueTask(), new PickupTask(), new TalismanTask());
    private boolean isRunning = true;
    private String status = null;
    public static Vars get() {
        return instance;
    }
    public void setStatus(String status) {
        Log.info(status);
        this.status = status;
    }

    // Talisman Specific
    private boolean bankClearOfAirTalismans = false;
    private int talismansCollected = 0;
    public void setBankClearOfAirTalismans(boolean bankClearOfAirTalismans) {
        this.bankClearOfAirTalismans = bankClearOfAirTalismans;
    }
    public void talismanCollected() {
        this.talismansCollected++;
    }
}