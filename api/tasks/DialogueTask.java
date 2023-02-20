package scripts.api.tasks;

import org.tribot.script.sdk.ChatScreen;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.util.TribotRandom;
import scripts.Priority;
import scripts.Task;
import scripts.api.data.Constants;

public class DialogueTask implements Task {
    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return ChatScreen.isOpen() &&
                !Inventory.contains(Constants.AIR_TALISMAN);
    }

    @Override
    public void execute() {

        // Select Required Option
        if (ChatScreen.containsOption(Constants.DUKE_DIALOGUE)) {

            Keyboard.typeString("1");

            if (!Waiting.waitUntil(TribotRandom.uniform(1200, 1800), () -> ChatScreen.containsMessage(Constants.DUKE_DIALOGUE))) {
                return;
            }
        }

        // Continue Chat
        if (ChatScreen.clickContinue()) {
            Waiting.waitUniform(400, 800);
        }
    }
}
