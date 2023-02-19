package scripts.api.data;

import lombok.Getter;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GroundItem;
import scripts.TaskSet;
import scripts.api.tasks.TalismanTask;
import scripts.api.tasks.BankTask;
import scripts.api.tasks.PickupTask;

import java.time.Duration;
import java.time.Instant;

@Getter
public class Vars {
    private static final Vars instance = new Vars();
    private final TaskSet tasks = new TaskSet(new BankTask(), new PickupTask(), new TalismanTask());
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
    private boolean pickingUpTalismans = false;
    private int talismansCollected = 0;
    private Instant talismansOnGroundSince = null;

    public void setBankClearOfAirTalismans(boolean bankClearOfAirTalismans) {
        this.bankClearOfAirTalismans = bankClearOfAirTalismans;
    }
    public void talismanCollected() {
        this.talismansCollected++;
    }
    public Instant getTalismansOnGroundSince() {
        return this.talismansOnGroundSince;
    }
    public void setTalismansOnGroundSince(Instant talismansOnGroundSince) {
        this.talismansOnGroundSince = talismansOnGroundSince;
    }
    public boolean shouldPickupTalismans() {

        if (pickingUpTalismans) {
            // We want to stop picking up talismans when:
            // There are no more talismans on the ground or
            // Our inventory is full
            if (talismansOnGround() == 0 ||
                    Inventory.isFull()) {

                pickingUpTalismans = false;
                return false;
            }
            return true;

        } else {
            // We want to pick up talismans when:
            // Talismans have been on the ground for nearly 2 minutes or
            // There are 27 talismans on ground and in inventory
            if (talismansOnGround() + Inventory.getCount(Constants.AIR_TALISMAN) >= 27 ||
                    (talismansOnGroundSince != null &&
                            Duration.between(talismansOnGroundSince, Instant.now()).toSeconds() >= 110 &&
                            talismansOnGround() > 0)) {

                pickingUpTalismans = true;
                setTalismansOnGroundSince(null); // Reset timer since we're picking them up
                return true;
            }
            return false;
        }
    }
    public int talismansOnGround() {
        return Query.groundItems()
                .idEquals(Constants.AIR_TALISMAN)
                .stream()
                .mapToInt(GroundItem::getStack)
                .sum();
    }
}