package entity;

import java.sql.Timestamp;

public class Text {

    private int id;
    private String modelName;
    private String originalText;
    private String promptText;
    private String outputText;

    public Text() {}

    public Text(String modelName, String originalText, String promptText, String outputText) {
        this.modelName = modelName;
        this.originalText = originalText;
        this.promptText = promptText;
        this.outputText = outputText;
    }

    public Text(int id, String modelName, String originalText, String promptText, String outputText) {
        this.id = id;
        this.modelName = modelName;
        this.originalText = originalText;
        this.promptText = promptText;
        this.outputText = outputText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }


}
