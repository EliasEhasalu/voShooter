package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerTookDamage {

    public int amount;
    public long dealerID;
    public boolean dealerIsPlayer;
    public long receiverID;

    public PlayerTookDamage() {
    }

    public PlayerTookDamage(int amount, long receiverID) {
        this.amount = amount;
        this.receiverID = receiverID;

        this.dealerIsPlayer = false;
    }

    public PlayerTookDamage(int amount, long receiverID, long dealerID) {
        this.amount = amount;
        this.receiverID = receiverID;
        this.dealerID = dealerID;

        this.dealerIsPlayer = true;
    }
}
