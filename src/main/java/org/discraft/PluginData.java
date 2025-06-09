package org.discraft;

import org.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class PluginData {

    private final Discraft discraft;

    public PluginData(Discraft discraftp) {
        this.discraft = discraftp;
    }

    public JSONObject loadData() {
        File file = new File(discraft.getDataFolder(), "discraft.json");

        if (!file.exists()) {
            createData(file);
            return new JSONObject();
        }

        try (FileReader reader = new FileReader(file)) {
            StringBuilder jsonContent = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                jsonContent.append((char) i);
            }
            return new JSONObject(jsonContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public void saveData(JSONObject jsonObject) {
        File file = new File(discraft.getDataFolder(), "discraft.json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFirstJoin(UUID playerUUID) {
        JSONObject data = loadData();

        return !data.has(playerUUID.toString());
    }


    public JSONObject initPlayer(UUID playerUUID) {
        JSONObject data = loadData();

        if (!data.has(playerUUID.toString())) {
            JSONObject playerData = new JSONObject();
            playerData.put("discordMute", false); // all set keys: discordMute
            data.put(playerUUID.toString(), playerData);
            saveData(data);
        }

        return data.getJSONObject(playerUUID.toString());
    }

    public void savePlayer(UUID playerUUID, JSONObject playerData) {
        JSONObject data = loadData();
        data.put(playerUUID.toString(), playerData);
        saveData(data);
    }

    private void createData(File file) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write("{}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
