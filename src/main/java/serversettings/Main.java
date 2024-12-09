package serversettings;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.plugin.PluginBase;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;

import java.io.FileReader;

public class Main extends PluginBase implements Listener {

    private Config c;

    private boolean usePlaceholderAPI;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        c = getConfig();
        saveResource("settings.json");
        usePlaceholderAPI = getServer().getPluginManager().isPluginEnabled(getServer().getPluginManager().getPlugin("PlaceholderAPI"));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onSettingsRequest(PlayerServerSettingsRequestEvent e) {
        ServerSettingsResponsePacket pk = new ServerSettingsResponsePacket();
        pk.formId = c.getInt("formID");

        try {
            JSONParser json = new JSONParser();
            Object obj = json.parse(new FileReader(getDataFolder() + "/settings.json"));
            JSONObject settings = (JSONObject) obj;
            pk.data = usePlaceholderAPI ? PlaceholderAPI.getInstance().translateString(settings.toString()) : settings.toString();
        } catch(Exception ex) {
            getLogger().error("Failed to parse settings.json", ex);
        }
        getServer().getScheduler().scheduleDelayedTask(this, () -> e.getPlayer().dataPacket(pk), 20);
    }

    @EventHandler
    public void onSettingsResponse(DataPacketReceiveEvent e) {
        if (e.getPacket() instanceof ModalFormResponsePacket && c.getBoolean("build-in-listener")) {
            ModalFormResponsePacket pk = (ModalFormResponsePacket) e.getPacket();
            if (pk.formId == c.getInt("formID")) {
                //TODO
            }
        }
    }
}
