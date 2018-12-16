package serversettings;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.plugin.PluginBase;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.FileReader;

public class Main extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        saveResource("settings.json");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onSettingsRequest(PlayerServerSettingsRequestEvent e) {
        ServerSettingsResponsePacket pk = new ServerSettingsResponsePacket();
        pk.formId = 5928;

        try {
            JSONParser json = new JSONParser();
            Object obj = json.parse(new FileReader(getDataFolder() + "/settings.json"));
            JSONObject settings = (JSONObject) obj;
            pk.data = settings.toString();
        } catch(Exception ex) {
            pk.data = "";
        }

        e.getPlayer().dataPacket(pk);
    }
}
