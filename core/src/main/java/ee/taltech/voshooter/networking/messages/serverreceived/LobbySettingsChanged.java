package ee.taltech.voshooter.networking.messages.serverreceived;

import ee.taltech.voshooter.map.GameMap;

public class LobbySettingsChanged {

    public int gameLength;
    public String lobbyCode;
    public int gameMode;
    public GameMap.MapType mapType;
    public int maxUsers;
    public int botAmount;

    /** Serialization. */
    public LobbySettingsChanged() {
    }

    public LobbySettingsChanged(int gameMode, GameMap.MapType mapType, int maxUsers, String lobbyCode, int gameLength,
                                int botAmount) {
        this.gameMode = gameMode;
        this.mapType = mapType;
        this.maxUsers = maxUsers;
        this.lobbyCode = lobbyCode;
        this.gameLength = gameLength;
        this.botAmount = botAmount;
    }
}
