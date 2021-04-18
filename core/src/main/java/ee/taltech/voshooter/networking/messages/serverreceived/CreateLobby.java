package ee.taltech.voshooter.networking.messages.serverreceived;

import ee.taltech.voshooter.map.GameMap;

public class CreateLobby {

    public int gameMode;
    public int maxPlayers;
    public GameMap.MapType mapType;

    /** Default constructor for serialization. */
    public CreateLobby() {
    }

    /**
     * @param gameMode The game mode of the lobby to be created.
     * @param maxPlayers The maximum amount of players the newly created lobby can hold.
     * @param mapType The game map used in the game.
     */
    public CreateLobby(int gameMode, int maxPlayers, GameMap.MapType mapType) {
        this.gameMode = gameMode;
        this.maxPlayers = maxPlayers;
        this.mapType = mapType;
    }
}
