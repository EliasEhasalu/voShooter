package ee.taltech.voshooter.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player x;

    /**
     * Set up.
     */
    @BeforeEach
    public void setup() {
        final int random = 3;

        x = new Player(random);
    }

    /**
     * .
     */
    @Test
    public void shouldReturnX() {
        x.getX();
    }
}
