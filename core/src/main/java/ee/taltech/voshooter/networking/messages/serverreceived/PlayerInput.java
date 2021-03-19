package ee.taltech.voshooter.networking.messages.serverreceived;

import ee.taltech.voshooter.controller.PlayerInputType;

import java.util.List;

public class PlayerInput {

    public List<PlayerInputType> inputs;

    /** Serialize. */
    public PlayerInput() {
    }

    /**
     * Construct the message.
     * @param inputs A list of inputs performed by the player.
     */
    public PlayerInput(List<PlayerInputType> inputs) {
        this.inputs = inputs;
    }
}
