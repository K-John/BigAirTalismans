package scripts.api.tasks;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.BankSettings;
import org.tribot.script.sdk.util.TribotRandom;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.Task;
import scripts.Priority;
import scripts.api.data.Constants;
import scripts.api.data.Vars;

public class BankTask implements Task {

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        // We have more than 10 talismans and we shouldn't be picking them up
        return (Inventory.getCount(Constants.AIR_TALISMAN) >= 10 && !Vars.get().shouldPickupTalismans()) ||
                Inventory.isFull() ||
                !Vars.get().isBankClearOfAirTalismans();
    }

    @Override
    public void execute() {

        // Walk to Bank
        if (!Constants.BANK.containsMyPlayer()) {

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
            Vars.get().setBankClearOfAirTalismans(false);

            if (!Bank.depositAll(Constants.AIR_TALISMAN) ||
                    !Waiting.waitUntil(TribotRandom.uniform(1800, 2400), () -> !Inventory.contains(Constants.AIR_TALISMAN))) {
                return;
            }
        }

        // Turn on Noted setting
        if (!BankSettings.isNoteEnabled()) {

            Vars.get().setStatus("Setting the bank to noted withdrawal.");

            if (!BankSettings.setNoteEnabled(true) ||
                    !Waiting.waitUntil(BankSettings::isNoteEnabled)) {
                return;
            }
        }

        // Withdraw Noted Air Talisman
        if (Bank.contains(Constants.AIR_TALISMAN)) {

            Vars.get().setStatus("Withdrawing noted Air Talismans.");
            Vars.get().setBankClearOfAirTalismans(false);

            if (!Bank.withdrawAll(Constants.AIR_TALISMAN) ||
                    !Waiting.waitUntil(TribotRandom.uniform(600, 1200), () -> !Bank.contains(Constants.AIR_TALISMAN))) {
                return;
            }
        }

        // Bank doesn't contain Air Talismans
        Vars.get().setBankClearOfAirTalismans(true);
    }
}
