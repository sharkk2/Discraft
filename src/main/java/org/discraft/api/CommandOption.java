package org.discraft.api;

public class CommandOption {
    public static final int STRING = 0;
    public static final int INTEGER = 1;
    public static final int BOOLEAN = 2;

    private final String name;
    private final int type;
    private final boolean required;
    private String description;
    private Object value;


    public CommandOption(String name, int type, boolean required) {
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
    public int getType() {return type;}
    public boolean isRequired() {return required;}
    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description;}
    public Object getValue() { return value; }
    public String getAsString() { return (String) value; }
    public int getAsInt() { return (int) value; }
    public boolean getAsBoolean() { return (boolean) value; }
}
