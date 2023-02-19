package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.painting.template.basic.BasicPaintTemplate;
import org.tribot.script.sdk.painting.template.basic.PaintLocation;
import org.tribot.script.sdk.painting.template.basic.PaintRows;
import org.tribot.script.sdk.painting.template.basic.PaintTextRow;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import scripts.api.data.Constants;
import scripts.api.data.Vars;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;

@TribotScriptManifest(name = "BigAirTalismans", author = "BigShot", category = "Runecrafting", description = "Gets Air Talismans from the start of Rune Mysteries.")
public class BigAirTalismans implements TribotScript {

    @Override
    public void execute(@NotNull final String args) {

        // We can safely assume there are no talismans in the bank if we have noted in our inventory
        if (Inventory.contains(Constants.NOTED_AIR_TALISMAN) && !Inventory.contains(Constants.AIR_TALISMAN)) {
            Vars.get().setBankClearOfAirTalismans(true);
        }

        // Paint
        PaintTextRow template = PaintTextRow.builder().background(Color.green.darker()).build();

        BasicPaintTemplate paint = BasicPaintTemplate.builder()
                .row(PaintRows.scriptName(template.toBuilder()))
                .row(PaintRows.runtime(template.toBuilder()))
                .row(template.toBuilder().label("Status").value(() -> Vars.get().getStatus()).build())
                .row(template.toBuilder().label("Collected").value(() -> Vars.get().getTalismansCollected()).build())
                .row(template.toBuilder().label("On Ground").value(() -> Vars.get().talismansOnGround()).build())
                .row(template.toBuilder().label("Should pickup").value(() -> Vars.get().shouldPickupTalismans()).build())
                .row(template.toBuilder().label("On Ground Since").value(() -> Duration.between(Vars.get().getTalismansOnGroundSince(), Instant.now()).toSeconds()).build())
                .location(PaintLocation.TOP_RIGHT_VIEWPORT)
                .build();

        Painting.addPaint(paint::render);

        // Script Loop
        while (Vars.get().isRunning()) {

            for (Task task : Vars.get().getTasks()) {
                if (task.validate()) {
                    task.execute();
                }
            }

            Waiting.waitUniform(20, 40);
        }
    }

}
