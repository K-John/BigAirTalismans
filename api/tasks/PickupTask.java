package scripts.api.tasks;

import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.util.TribotRandom;
import scripts.Task;
import scripts.Priority;
import scripts.api.data.Constants;
import scripts.api.data.Vars;

public class PickupTask implements Task {

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return Vars.get().shouldPickupTalismans();
    }

    @Override
    public void execute() {
        Vars.get().setStatus("Picking up Air Talismans");

        // Wait until we're done moving to click another
        if (MyPlayer.isMoving()) {
            Waiting.waitUntil(TribotRandom.uniform(1200, 1800), () -> !MyPlayer.isMoving());
        }

        // Pick up Talisman
        Query.groundItems()
                .inArea(Constants.DUKES_ROOM)
                .idEquals(Constants.NOTED_AIR_TALISMAN, Constants.AIR_TALISMAN)
                .findBestInteractable()
                .map(talisman -> talisman.interact("Take"));
    }
}
