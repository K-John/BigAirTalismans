package scripts.api.classes;

import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.painting.template.basic.BasicPaintTemplate;
import org.tribot.script.sdk.painting.template.basic.PaintLocation;
import org.tribot.script.sdk.painting.template.basic.PaintRows;
import org.tribot.script.sdk.painting.template.basic.PaintTextRow;
import scripts.api.data.Vars;

import java.awt.*;

public class Paint {

    public static void execute() {

        PaintTextRow template = PaintTextRow.builder().background(Color.green.darker()).build();

        BasicPaintTemplate.BasicPaintTemplateBuilder paintBuilder = BasicPaintTemplate.builder()
                .row(PaintRows.scriptName(template.toBuilder()))
                .row(PaintRows.runtime(template.toBuilder()))
                .row(template.toBuilder().label("Status").value(() -> Vars.get().getStatus()).build())
                .row(template.toBuilder().label("Collected").value(() -> Vars.get().getCollectedCount()).build());

        // Add Extra Information
        if (Vars.get().isDebug()) {

            paintBuilder.row(template.toBuilder().label("Dropped Count").value(Talisman::droppedCount).build())
                    .row(template.toBuilder().label("Should Pickup").value(Talisman::shouldBePickedUp).build())
                    .row(template.toBuilder().label("Seconds Dropped").value(Talisman::secondsDroppedFor).build())
                    .row(template.toBuilder().label("Bank Is Clear").value(() -> Vars.get().isBankClear()).build());
        }

        BasicPaintTemplate paint = paintBuilder.location(PaintLocation.TOP_RIGHT_VIEWPORT).build();

        Painting.addPaint(paint::render);
    }
}
