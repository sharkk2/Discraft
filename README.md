# Discraft
A discord-minecraft paper plugin.

# Current features (1.4)
1. Mixed minecraft and discord chatting.
2. Console integration with discord.
3. Shows server data in discord like: Current online players and server latency and tps.
4. Control players from a discord command with supported actions: kill, heal, starve, feed, teleport, view inventory, toggle OP.
5. Choose to toggle the discord messages from minecraft using the discordmute command
6. Profanity filter

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
