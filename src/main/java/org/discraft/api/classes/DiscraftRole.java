package org.discraft.api.classes;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.discraft.api.classes.actions.MessageActions;
import org.discraft.api.classes.actions.RoleActions;

import java.util.EnumSet;
import java.util.function.Consumer;

public class DiscraftRole {
    private final long id;
    private final long guildID;
    private final String name;
    private final int color;
    private final long permissionRaw;
    private final int position;
    private final RoleActions actions;
    public enum Permission {
        //https://docs.discord.com/developers/topics/permissions#permissions-bitwise-permission-flags
        CREATE_INSTANT_INVITE(1L << 0),
        KICK_MEMBERS(1L << 1),
        BAN_MEMBERS(1L << 2),
        ADMINISTRATOR(1L << 3),
        MANAGE_CHANNELS(1L << 4),
        MANAGE_GUILD(1L << 5),
        ADD_REACTIONS(1L << 6),
        VIEW_AUDIT_LOG(1L << 7),
        VIEW_CHANNEL(1L << 10),
        SEND_MESSAGES(1L << 11),
        SEND_TTS_MESSAGES(1L << 12),
        MANAGE_MESSAGES(1L << 13),
        EMBED_LINKS(1L << 14),
        ATTACH_FILES(1L << 15),
        READ_MESSAGE_HISTORY(1L << 16),
        MENTION_EVERYONE(1L << 17),
        USE_EXTERNAL_EMOJIS(1L << 18),
        CONNECT(1L << 20),
        SPEAK(1L << 21),
        MUTE_MEMBERS(1L << 22),
        DEAFEN_MEMBERS(1L << 23),
        MOVE_MEMBERS(1L << 24),
        CHANGE_NICKNAME(1L << 26),
        MANAGE_NICKNAMES(1L << 27),
        MANAGE_ROLES(1L << 28),
        MANAGE_WEBHOOKS(1L << 29),
        MANAGE_EMOJIS(1L << 30),
        USE_SLASH_COMMANDS(1L << 31),
        MANAGE_THREADS(1L << 34),
        CREATE_PUBLIC_THREADS(1L << 35),
        CREATE_PRIVATE_THREADS(1L << 36),
        SEND_MESSAGES_IN_THREADS(1L << 38),
        MODERATE_MEMBERS(1L << 40);

        private final long bit;
        Permission(long bit) {this.bit = bit;}
        public long getBit() {return bit;}
        public static EnumSet<Permission> fromRaw(long raw) {
            EnumSet<Permission> perms = EnumSet.noneOf(Permission.class);
            for (Permission p : values()) {if ((raw & p.bit) != 0) perms.add(p);}
            return perms;
        }
    }

    public DiscraftRole(
            long id, long guildID,
            String name, int color, long permissionRaw, int position,
            RoleActions actions
    ) {
        this.id = id;
        this.guildID = guildID;
        this.name = name;
        this.color = color;
        this.permissionRaw = permissionRaw;
        this.position = position;
        this.actions = actions;
    }

    public long getID() {return id;}
    public void delete() {actions.delete().run();}
    public int getColor() {return color;}
    public String getName() {return name;}
    public long getGuildID() {return guildID;}
    public long getPermissionRaw() {return permissionRaw;}
    public int getPosition() {return position;}
    public static RoleActions _bakeActions(Role role) {
        return new RoleActions(
                () -> role.delete().queue()
        );
    }
}
