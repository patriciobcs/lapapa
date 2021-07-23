package com.lapapa.app.chatBotForm;

public class Field {
    public String id;
    public String name;
    public String type;
    public String value;
    public Field(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    public boolean empty() {
        return this.value == null || this.value.isEmpty();
    }
    public boolean valid(String value) {
        return true;
    }
    public String JSONField() { return "'" + this.name + "':'" + this.value + "'";}
}
