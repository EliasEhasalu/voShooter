package ee.taltech.voshooter.networking.messages.serverreceived;

import ee.taltech.voshooter.controller.PlayerAction;

import java.util.List;

public class PlayerInput {

    public List<PlayerAction> inputs;

    /** */
    public PlayerInput() {
    }

    /**
     * Construct the message.
     * @param inputs A list of inputs performed by the player.
     */
    public PlayerInput(List<PlayerAction> inputs) {
        this.inputs = inputs;
    }
}
