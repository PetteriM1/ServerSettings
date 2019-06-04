package serversettings;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.FileReader;

public class Main extends PluginBase implements Listener {

    Config c;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        c = getConfig();
        saveResource("settings.json");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onSettingsRequest(PlayerServerSettingsRequestEvent e) {
        ServerSettingsResponsePacket pk = new ServerSettingsResponsePacket();
        pk.formId = c.getInt("formID");

        try {
            Object obj = new JSONParser().parse(new FileReader(getDataFolder() + "/settings.json"));
            JSONObject settings = (JSONObject) obj;
            pk.data = settings.toString();
        } catch(Exception ex) {
            pk.data = "";
        }

        e.getPlayer().dataPacket(pk);
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
