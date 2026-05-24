package org.discraft.api.classes;

import java.util.function.Consumer;

public class DiscraftUser {
    private final long id;
    private final String name;
    private final String avatarUrl;

    public DiscraftUser(long id, String name, String avatarURL) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarURL;
    }

    public long getID() {return id;}
    public String getName() {return name;}
    public String getAvatarURL() {return avatarUrl;}
    public String mention() {return "<@" + id + ">";} // just a shortcut
}
