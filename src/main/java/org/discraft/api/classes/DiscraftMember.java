package org.discraft.api.classes;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.discraft.api.DiscraftAPI;
import org.discraft.api.classes.actions.MemberActions;

import java.util.List;

public class DiscraftMember {
    private final long id;
    private final String name;
    private final String avatarUrl;
    private final String nickname;
    private final List<DiscraftRole> roles;
    private final MemberActions actions;

    public DiscraftMember(long id, String name, String avatarUrl, String nickname, List<DiscraftRole> roles, MemberActions actions) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.roles = roles;
        this.actions = actions;
    }

    public long getID() { return id; }
    public String getName() { return name; }
    public String getAvatarURL() { return avatarUrl; }
    public String getNickname() { return nickname; }
    public List<DiscraftRole> getRoles() { return roles; }
    public String mention() { return "<@" + id + ">"; }
    public void setNickname(String nickname) { actions.nicknamer().accept(nickname); }
    public void sendDM(String message) { actions.sender().accept(message); }
    public void addRole(DiscraftRole role) {actions.roleAdder().accept(role);}
    public void removeRole(DiscraftRole role) {actions.roleRemover().accept(role);}


    public static MemberActions _bakeActions(Member member) {
        if (member == null) return null;
        Guild guild = member.getGuild();
        return new MemberActions(
                nickname -> guild.modifyNickname(member, nickname).queue(),
                message -> member.getUser().openPrivateChannel().queue(channel -> {channel.sendMessage(message).queue();}),
                role -> member.getGuild().addRoleToMember(member, member.getGuild().getRoleById(role.getID())),
                role -> member.getGuild().removeRoleFromMember(member, member.getGuild().getRoleById(role.getID()))
        );
    }
}