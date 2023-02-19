package scripts.api.tasks;

import org.tribot.script.sdk.query.Query;
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

        Query.groundItems()
                .idEquals(Constants.NOTED_AIR_TALISMAN, Constants.AIR_TALISMAN)
                .findBestInteractable()
                .map(talisman -> talisman.interact("Take"));
    }
}
