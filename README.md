# Discraft
A discord-minecraft paper plugin.

# Current features (1.5)
1. Mixed minecraft and discord chatting.
2. Console integration with discord.
3. Shows server data in discord like: Current online players and server latency and tps.
4. Control players from a discord command with supported actions: kill, heal, starve, feed, teleport, view inventory, toggle OP.
5. Choose to toggle the discord messages from minecraft using the discordmute command
6. Profanity filter
7. A Discord integration API for other plugins

# Configuration
Inside the server's plugins folder, a Discraft folder will be generated inside is a `config.yml` file
```yml
token: "Discord bot token"
admins:
  - "1092548532180877415"  # Example Discord user ID of an admin
  - "77489499272839170"  # Another example of an admin ID
guild_id: "1327003898338349128" # Discord server id

chat_channel: "1387011007968448513" # The important discord channel ids
console_channel: "1387748260638363658"

allow_profanity: false # Profanity filter

# Bot profile settings
status: ""
activity_type: "1" # 0: Playing, 1: Listening, 2: Watching

# Other settings
chat_to_discord: true
console_to_discord: true
discord_to_minecraft: true

player_join: true
player_leave: true
player_death: true
player_advancements: true
server_on: true
server_off: true

reactOnSuccess: true
debug: false
```

# Discraft API Documentation

## Setup

Compile your plugin against any Discraft version **> 1.4** and add `Discraft` as a dependency in your plugin manifest.

## Accessing the API

*Requires Discraft 1.5+*

```java
DiscraftAPI discraftAPI = DiscraftAPI.get();
```

`DiscraftAPI` exposes the following methods:

| Method | Description |
|--------|-------------|
| `registerCommand(String name, String description, List<CommandOption> options, BiConsumer<List<CommandOption>, CommandContext> callback)` | Registers a slash command |
| `getChannel(long id)` | Returns a `DiscraftChannel` by ID |
| `getUser(long id)` | Returns a `DiscraftUser` by ID |
| `getMember(long guildID, long userID)` | Returns a `DiscraftMember` by guild and user ID. Throws `IllegalArgumentException` if either isn't found |

---

## Commands

### Registering a Command

```java
discraftAPI.registerCommand(
    "echo",
    "Echo a message",
    List.of(new CommandOption("toecho", CommandOption.OPTION_TYPE.STRING, true)),
    (options, ctx) -> {
        ctx.respond(options.get(0).getAsString() + "\n\nFrom: " + ctx.getUser().mention(), false);
    }
);
```

### CommandOption

Create an option with:

```java
new CommandOption(String name, CommandOption.OPTION_TYPE type, boolean required)
```

**Option types:**

| Constant | Description |
|----------|-------------|
| `CommandOption.OPTION_TYPE.STRING` | Text value |
| `CommandOption.OPTION_TYPE.INTEGER` | Integer value |
| `CommandOption.OPTION_TYPE.BOOLEAN` | True/false value |
| `CommandOption.OPTION_TYPE.USER` | Discord user |
| `CommandOption.OPTION_TYPE.CHANNEL` | Discord channel |
| `CommandOption.OPTION_TYPE.ROLE` | Discord role |

**Methods:**

| Method | Description |
|--------|-------------|
| `getName()` | Get the option name |
| `getType()` | Get the option type |
| `setDescription(String)` | Set an optional description (returns `CommandOption` for chaining) |
| `getDescription()` | Get the option description |
| `isRequired()` | Whether the option is required |
| `getValue()` | Raw value as `Object` (use in callbacks) |
| `getAsString()` | Value cast to `String` |
| `getAsInt()` | Value cast to `int` |
| `getAsBoolean()` | Value cast to `boolean` |
| `getAsUser()` | Value cast to `DiscraftUser` |
| `getAsChannel()` | Value cast to `DiscraftChannel` |

### CommandContext

Passed as the second argument to your command callback. Used to inspect and respond to the interaction.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getUser()` | `DiscraftUser` | The user who triggered the command |
| `getChannel()` | `DiscraftChannel` | The channel where the command was triggered |
| `getInteractionTime()` | `long` | Epoch seconds of when the interaction occurred |
| `respond(String message, boolean ephemeral)` | `void` | Sends a response to the command. Pass `true` for ephemeral to make it only visible to the user who triggered it |

---

## Classes

### DiscraftUser

Represents a Discord user.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `long` | The user's Discord ID |
| `getName()` | `String` | The user's username |
| `getAvatarURL()` | `String` | URL to the user's avatar |
| `mention()` | `String` | A mention string, e.g. `@username` |
| `setNickName(String nickName, long guildID)` | `void` | Sets the user's nickname in the specified guild |
| `sendDM(String message)` | `void` | Sends a direct message to the user |

---

### DiscraftMember

Represents a Discord guild member (a user within a specific server).

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `long` | The member's Discord ID |
| `getName()` | `String` | The member's username |
| `getAvatarURL()` | `String` | URL to the member's avatar |
| `getNickname()` | `String` | The member's server nickname |
| `getRoles()` | `List<DiscraftRole>` | The member's roles in the guild |
| `mention()` | `String` | A mention string, e.g. `@username` |
| `setNickname(String nickname)` | `void` | Sets the member's server nickname |
| `sendDM(String message)` | `void` | Sends a direct message to the member |
| `addRole(DiscraftRole role)` | `void` | Adds a role to the member |
| `removeRole(DiscraftRole role)` | `void` | Removes a role from the member |

---

### DiscraftRole

Represents a Discord role in a guild.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `long` | The role's Discord ID |
| `getGuildID()` | `long` | The ID of the guild this role belongs to |
| `getName()` | `String` | The role's name |
| `getColor()` | `int` | The role's color as an RGB integer |
| `getPermissionRaw()` | `long` | The raw permission bitfield |
| `getPosition()` | `int` | The role's position in the hierarchy |
| `delete()` | `void` | Deletes the role |

**Permissions:**

Use `DiscraftRole.Permission.fromRaw(long raw)` to convert a raw permission bitfield into a set of `Permission` enum constants:

```java
EnumSet<DiscraftRole.Permission> perms = DiscraftRole.Permission.fromRaw(role.getPermissionRaw());
if (perms.contains(DiscraftRole.Permission.ADMINISTRATOR)) {
    // do something
}
```

Available `Permission` values: `CREATE_INSTANT_INVITE`, `KICK_MEMBERS`, `BAN_MEMBERS`, `ADMINISTRATOR`, `MANAGE_CHANNELS`, `MANAGE_GUILD`, `ADD_REACTIONS`, `VIEW_AUDIT_LOG`, `VIEW_CHANNEL`, `SEND_MESSAGES`, `SEND_TTS_MESSAGES`, `MANAGE_MESSAGES`, `EMBED_LINKS`, `ATTACH_FILES`, `READ_MESSAGE_HISTORY`, `MENTION_EVERYONE`, `USE_EXTERNAL_EMOJIS`, `CONNECT`, `SPEAK`, `MUTE_MEMBERS`, `DEAFEN_MEMBERS`, `MOVE_MEMBERS`, `CHANGE_NICKNAME`, `MANAGE_NICKNAMES`, `MANAGE_ROLES`, `MANAGE_WEBHOOKS`, `MANAGE_EMOJIS`, `USE_SLASH_COMMANDS`, `MANAGE_THREADS`, `CREATE_PUBLIC_THREADS`, `CREATE_PRIVATE_THREADS`, `SEND_MESSAGES_IN_THREADS`, `MODERATE_MEMBERS`

---

### DiscraftChannel

Represents a Discord channel.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `long` | The channel's Discord ID |
| `getGuildID()` | `long` | The ID of the guild this channel belongs to |
| `getName()` | `String` | The channel's name |
| `sendMessage(String message)` | `void` | Sends a message to the channel |
| `getMessage(long messageID)` | `CompletableFuture<DiscraftMessage>` | Fetches a message by ID asynchronously |

**Example: fetching and replying to a message:**

```java
DiscraftAPI api = DiscraftAPI.get();

api.getChannel(123456789L).getMessage(987654321L).thenAccept(message -> {
    System.out.println(message.getAuthor().getName() + ": " + message.getContent());
    message.reply("Got your message!");
});
```

---

### DiscraftMessage

Represents a Discord message.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `long` | The message's Discord ID |
| `getChannel()` | `DiscraftChannel` | The channel the message was sent in |
| `getAuthor()` | `DiscraftUser` | The user who sent the message |
| `getContent()` | `String` | The text content of the message |
| `getTimeCreated()` | `long` | Epoch seconds of when the message was created |
| `delete()` | `void` | Deletes the message |
| `addReaction(String emoji)` | `void` | Reacts to the message with an emoji |
| `removeReaction(String emoji)` | `void` | Removes a reaction from the message |
| `reply(String message)` | `void` | Replies to the message |
