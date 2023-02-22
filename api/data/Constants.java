package scripts.api.data;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class Constants {

    public static final int AIR_TALISMAN = 1438;
    public static final int NOTED_AIR_TALISMAN = 1439;
    public static final int DUKE = 815;
    public static final Area DUKES_ROOM = Area.fromRectangle(new WorldTile(3209, 3219, 1), new WorldTile(3212, 3224, 1));
    public static final Area BANK = Area.fromRectangle(new WorldTile(3207, 3217, 2), new WorldTile(3210, 3220, 2));
    public static final String DUKE_DIALOGUE = "What did you want me to do again?";
}