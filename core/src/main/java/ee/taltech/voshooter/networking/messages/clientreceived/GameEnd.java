package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.map.GameMap;

import java.util.List;

public class GameEnd {

    public int gameMode;
    public int gameLength;
    public int playerCount;
    public int botAmount;
    public GameMap.MapType mapType;
    public List<String> leaderBoard;

    /** Serialization. */
    public GameEnd() {
    }

    public GameEnd(int gameMode, int gameLength, int playerCount, int botAmount, GameMap.MapType mapType,
                   List<String> leaderBoard) {
        this.gameMode = gameMode;
        this.gameLength = gameLength;
        this.playerCount = playerCount;
        this.botAmount = botAmount;
        this.mapType = mapType;
        this.leaderBoard = leaderBoard;
    }
}
