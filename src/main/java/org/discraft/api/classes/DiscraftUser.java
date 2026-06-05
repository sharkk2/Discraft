package org.discraft.api.classes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.discraft.api.classes.actions.UserActions;

import java.util.List;
import java.util.function.Consumer;

public class DiscraftUser {
    private final long id;
    private final String name;
    private final String avatarUrl;
    private final UserActions actions;

    public DiscraftUser(long id, String name, String avatarURL, UserActions actions) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarURL;
        this.actions = actions;
    }

    public long getID() {return id;}
    public String getName() {return name;}
    public String getAvatarURL() {return avatarUrl;}
    public String mention() {return "<@" + id + ">";} // just a shortcut
    public void setNickName(String nickName, long guildID) {actions.nicknamer().accept(nickName, guildID);}
    public void sendDM(String message) {actions.sender().accept(message);}

    public static UserActions _bakeActions(User user) {
        if (user == null) return null;
        return new UserActions(
                (nickname, guildid) -> {
                    Guild guild = user.getJDA().getGuildById(guildid);
                    if (guild == null) return;
                    guild.retrieveMember(user).queue(member -> guild.modifyNickname(member, nickname).queue());
                },
                message -> {
                    user.openPrivateChannel().queue(channel -> {
                        channel.sendMessage(message).queue();
                    });
                }
        );
    }
}
