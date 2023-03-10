package scripts.api.tasks;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.util.TribotRandom;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.Task;
import scripts.Priority;
import scripts.api.classes.Talisman;
import scripts.api.data.Constants;
import scripts.api.data.Vars;

import java.time.Instant;

public class TalismanTask implements Task {

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return Inventory.getCount(Constants.AIR_TALISMAN) < 10 &&
                !Talisman.shouldBePickedUp() &&
                (!ChatScreen.isOpen() || Inventory.contains(Constants.AIR_TALISMAN)) &&
                Vars.get().isBankClear();
    }

    @Override
    public void execute() {

        // Walk to Duke's Room
        if (!Constants.DUKES_ROOM.containsMyPlayer()) {

            // Enable Run
            if (!Options.isRunEnabled() &&
                    MyPlayer.getRunEnergy() > TribotRandom.uniform(70, 100)) {
                Options.setRunEnabled(true);
            }

            Vars.get().setStatus("Walking to Duke's room.");

            if (!GlobalWalking.walkTo(Constants.DUKES_ROOM.getRandomTile()) ||
                    !Waiting.waitUntil(Constants.DUKES_ROOM::containsMyPlayer)) {
                return;
            }
        }

        // Drop talismans
        if (Inventory.contains(Constants.AIR_TALISMAN)) {

            Vars.get().setStatus("Dropping Air Talismans.");

            if (Inventory.drop(Constants.AIR_TALISMAN) > 0 ||
                    Waiting.waitUntil(TribotRandom.uniform(1800, 2400), () -> Inventory.contains(Constants.AIR_TALISMAN))) {

                // This is our first dropped talisman, start the timer
                if (Talisman.timeDropped == null) {
                    Talisman.timeDropped = Instant.now();
                }
            } else {
                return;
            }
        }

        // Talk to Duke to get a talisman
        if (talkToDuke() && Waiting.waitUntil(ChatScreen::isOpen)) {

            Vars.get().setStatus("Talking to Duke.");
        }
    }

    private boolean talkToDuke() {
        return Query.npcs()
                .idEquals(Constants.DUKE)
                .findBestInteractable()
                .map(cook -> cook.interact("Talk-to"))
                .orElse(false);
    }
}
