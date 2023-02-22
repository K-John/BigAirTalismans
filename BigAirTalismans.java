package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Quest;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import scripts.api.classes.Talisman;
import scripts.api.classes.Paint;
import scripts.api.data.Vars;

@TribotScriptManifest(name = "BigAirTalismans", author = "BigShot", category = "Runecrafting", description = "Gets Air Talismans from the start of Rune Mysteries.")
public class BigAirTalismans implements TribotScript {

    @Override
    public void execute(@NotNull final String args) {

        validate();
        configure(args);
        Paint.execute();

        // Script Loop
        while (Vars.get().isRunning()) {

            Talisman.checkIfShouldPickUp();

            for (Task task : Vars.get().getTasks()) {
                if (task.validate()) {
                    task.execute();
                }
            }

            Waiting.waitUniform(20, 40);
        }
    }

    public void validate() {

        if (Quest.RUNE_MYSTERIES.getState() != Quest.State.IN_PROGRESS) {

            Log.error("The Rune Mysteries Quest needs to be in progress for this script to work.");
            Vars.get().stopRunning();
        }
    }

    public void configure(String args) {

        String[] argArray = args.trim().split("\\s+");

        for (String arg : argArray) {
            switch(arg) {
                case "debug":
                    Vars.get().setDebug(true);
            }
        }
    }
}
