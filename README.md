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

---

## Commands

### Registering a Command

```java
discraftAPI.registerCommand(
    "echo",
    "Echo a message",
    List.of(new CommandOption("toecho", CommandOption.STRING, true)),
    (options, ctx) -> {
        ctx.respond(options.get(0).getAsString() + "\n\nFrom: " + ctx.user.mention());
    }
);
```

### CommandOption

Create an option with:

```java
new CommandOption(String name, int type, boolean required)
```

**Option types:**

| Constant | Description |
|----------|-------------|
| `CommandOption.STRING` | Text value |
| `CommandOption.INTEGER` | Integer value |
| `CommandOption.BOOLEAN` | True/false value |

**Methods:**

| Method | Description |
|--------|-------------|
| `getName()` / `setName(String)` | Get or set the option name |
| `getType()` / `setType(int)` | Get or set the option type |
| `setDescription(String)` | Set an optional description |
| `isRequired()` | Whether the option is required |
| `getValue()` | Raw value as `Object` (use in callbacks) |
| `getAsString()` | Value cast to `String` |
| `getAsInteger()` | Value cast to `Integer` |
| `getAsBoolean()` | Value cast to `Boolean` |

### CommandContext

Passed as the second argument to your command callback. Used to inspect and respond to the interaction.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getUser()` | `DiscraftUser` | The user who triggered the command |
| `getChannel()` | `DiscraftChannel` | The channel where the command was triggered |
| `getInteractionTime()` | `long` | Epoch seconds of when the interaction occurred |
| `respond(String msg)` | `void` | Sends a response to the command |

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

---

### DiscraftChannel

Represents a Discord channel.

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `long` | The channel's Discord ID |
| `getName()` | `String` | The channel's name |
| `sendMessage(String message)` | `void` | Sends a message to the channel |
| `getMessage(long messageID, Consumer<DiscraftMessage> callback)` | `void` | Fetches a message by ID and passes it to the callback |

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
| `react(String emoji)` | `void` | Reacts to the message with an emoji |
| `reply(String message)` | `void` | Replies to the message |

**Example: fetching and replying to a message:**

```java
DiscraftAPI api = DiscraftAPI.get();

api.getChannel(123456789L).getMessage(987654321L, message -> {
    System.out.println(message.getAuthor().getName() + ": " + message.getContent());
    message.reply("Got your message!");
});
```
