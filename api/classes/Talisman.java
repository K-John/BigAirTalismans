package scripts.api.classes;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GroundItem;
import org.tribot.script.sdk.util.TribotRandom;
import scripts.api.data.Constants;

import java.time.Duration;
import java.time.Instant;

public class Talisman {

    public static Instant timeDropped = null;
    private static boolean readyToBePickedUp = false;

    public static boolean shouldBePickedUp() {
        return readyToBePickedUp;
    }

    public static Long secondsDroppedFor() {
        if (timeDropped == null) {
            return 0L;
        }

        return Duration.between(timeDropped, Instant.now()).toSeconds();
    }

    public static int droppedCount() {
        return Query.groundItems()
                .inArea(Constants.DUKES_ROOM)
                .idEquals(Constants.AIR_TALISMAN)
                .stream()
                .mapToInt(GroundItem::getStack)
                .sum();
    }

    public static void checkIfShouldPickUp() {

        if (readyToBePickedUp) {
            // We want to stop picking up talismans when:
            // There are no more talismans on the ground or
            // Our inventory is full
            if (droppedCount() == 0 ||
                    Inventory.isFull()) {

                readyToBePickedUp = false;
            }

        } else {
            // We want to pick up talismans when:
            // Talismans have been on the ground for nearly 2 minutes or
            // There are 27 talismans on ground and in inventory
            if (droppedCount() + Inventory.getCount(Constants.AIR_TALISMAN) >= 27 ||
                    (secondsDroppedFor() >= TribotRandom.uniform(100, 115) && droppedCount() > 0)) {

                readyToBePickedUp = true;
                timeDropped = null; // Reset timer since we're picking them up
            }
        }
    }
}
