package ee.taltech.voshooter.networking.messages.serverreceived;

import ee.taltech.voshooter.map.GameMap;

public class LobbySettingsChanged {

    public String lobbyCode;
    public int gameMode;
    public GameMap.MapType mapType;
    public int maxUsers;

    /** Serialization. */
    public LobbySettingsChanged() {
    }

    public LobbySettingsChanged(int gameMode, GameMap.MapType mapType, int maxUsers, String lobbyCode) {
        this.gameMode = gameMode;
        this.mapType = mapType;
        this.maxUsers = maxUsers;
        this.lobbyCode = lobbyCode;
    }
}
