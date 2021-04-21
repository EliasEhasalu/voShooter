package ee.taltech.voshooter.gamestate;

public class ChatEntry {

    public static final float MAX_DURATION = 600;
    private float duration = MAX_DURATION;
    private String text;
    private String prefix;

    /**
     * Constructor.
     */
    public ChatEntry() {
    }

    public boolean tick() {
        duration -= 1;
        return duration <= 0;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = "[" + prefix + "]: ";
    }
}
