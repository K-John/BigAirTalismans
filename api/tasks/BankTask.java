package scripts.api.tasks;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.util.TribotRandom;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.Task;
import scripts.Priority;
import scripts.api.classes.Talisman;
import scripts.api.data.Constants;
import scripts.api.data.Vars;

public class BankTask implements Task {

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return (Inventory.getCount(Constants.AIR_TALISMAN) >= 10 && !Talisman.shouldBePickedUp()) ||
                Inventory.isFull() ||
                !Vars.get().isBankClear();
    }

    @Override
    public void execute() {

        // Walk to Bank
        if (!Constants.BANK.containsMyPlayer()) {

            // Enable Run
            if (!Options.isRunEnabled() &&
                    MyPlayer.getRunEnergy() > TribotRandom.uniform(70, 100)) {
                Options.setRunEnabled(true);
            }

            Vars.get().setStatus("Walking to the bank.");

            if (!GlobalWalking.walkTo(Constants.BANK.getRandomTile()) ||
                    !Waiting.waitUntil(Constants.BANK::containsMyPlayer)) {
                return;
            }
        }

        // Open Bank
        if (!Bank.isOpen()) {

            Vars.get().setStatus("Opening the bank.");

            if (!Bank.open() ||
                    !Waiting.waitUntil(Bank::isOpen)) {
                return;
            }
        }

        // Deposit Un-Noted Air Talismans
        if (Inventory.contains(Constants.AIR_TALISMAN)) {

            Vars.get().setStatus("Depositing Air Talismans");
            Vars.get().setBankClear(false);

            if (!Bank.depositAll(Constants.AIR_TALISMAN) ||
                    !Waiting.waitUntil(TribotRandom.uniform(1800, 2400), () -> !Inventory.contains(Constants.AIR_TALISMAN))) {
                return;
            }
        }

        // Withdraw Noted Air Talisman
        if (Bank.contains(Constants.AIR_TALISMAN)) {

            // Turn on Noted setting
            if (!BankSettings.isNoteEnabled()) {

                Vars.get().setStatus("Setting the bank to noted withdrawal.");

                if (!BankSettings.setNoteEnabled(true) ||
                        !Waiting.waitUntil(BankSettings::isNoteEnabled)) {
                    return;
                }
            }

            Vars.get().setStatus("Withdrawing noted Air Talismans.");
            Vars.get().setBankClear(false);

            if (!Bank.withdrawAll(Constants.AIR_TALISMAN) ||
                    !Waiting.waitUntil(TribotRandom.uniform(600, 1200), () -> !Bank.contains(Constants.AIR_TALISMAN))) {
                return;
            }
        }
        // Bank doesn't contain Air Talismans
        Vars.get().setBankClear(true);

        // Set Starting Count
        if (Vars.get().getStartingCount() == -1) {
            Vars.get().setStartingCount(Inventory.getCount(Constants.NOTED_AIR_TALISMAN));
        }
    }
}
