package org.discraft.api;

import org.discraft.api.classes.DiscraftChannel;
import org.discraft.api.classes.DiscraftUser;

public class CommandOption {
    public enum OPTION_TYPE {
        STRING, INTEGER, BOOLEAN, USER, CHANNEL, ROLE
    }

    private final String name;
    private final OPTION_TYPE type;
    private final boolean required;
    private String description;
    private Object value;


    public CommandOption(String name, OPTION_TYPE type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
        description = name;
    }

    public CommandOption answer(Object value) {
        this.value = value;
        return this;
    }

    public String getName() {return name;}
    public OPTION_TYPE getType() {return type;}
    public boolean isRequired() {return required;}
    public CommandOption setDescription(String description) {this.description = description; return this;}
    public String getDescription() {return description;}
    public Object getValue() { return value; }
    public String getAsString() { return (String) value; }
    public DiscraftUser getAsUser() { return (DiscraftUser) value; }
    public DiscraftChannel getAsChannel() { return (DiscraftChannel) value; }


    public int getAsInt() { return (int) value; }
    public boolean getAsBoolean() { return (boolean) value; }
}
