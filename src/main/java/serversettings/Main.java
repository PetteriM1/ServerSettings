package serversettings;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.plugin.PluginBase;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;

import java.io.FileReader;

public class Main extends PluginBase implements Listener {

    private static String DATA;

    @Override
    public void onEnable() {
        saveResource("settings.json");
        try {
            JSONParser json = new JSONParser();
            Object obj = json.parse(new FileReader(getDataFolder() + "/settings.json"));
            JSONObject settings = (JSONObject) obj;
            DATA = settings.toString();
        } catch(Exception ex) {
            getLogger().error("Failed to parse settings.json", ex);
            DATA = "";
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSettingsRequest(PlayerServerSettingsRequestEvent e) {
        ServerSettingsResponsePacket pk = new ServerSettingsResponsePacket();
        pk.formId = 5928;
        pk.data = DATA;
        getServer().getScheduler().scheduleDelayedTask(this, () -> e.getPlayer().dataPacket(pk), 20);
    }
}
