package ee.taltech.voshooter.settingsinputs;

public class SettingsInput {

    private static Integer inputKey;
    private static Integer inputButton;

    /**
     * Getter for input key.
     * @return input key
     */
    public static Integer getInputKey() {
        return inputKey;
    }

    /**
     * Setter for input key.
     * @param inputKey that was pressed down.
     */
    public static void setInputKey(int inputKey) {
        SettingsInput.inputKey = inputKey;
    }

    /**
     * Remove input key when key is not pressed.
     */
    public static void removeInputKey() {
        SettingsInput.inputKey = null;
    }

    /**
     * @return input button.
     */
    public static Integer getInputButton() {
        return inputButton;
    }

    /**
     * @param inputButton new input.
     */
    public static void setInputButton(int inputButton) {
        SettingsInput.inputButton = inputButton;
    }

    /**
     * Remove the button when released.
     */
    public static void removeInputButton() {
        SettingsInput.inputButton = null;
    }
}
