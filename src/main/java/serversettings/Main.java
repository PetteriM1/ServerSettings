package serversettings;

import net.minidev.json.JSONObject;
import java.io.FileReader;
import net.minidev.json.parser.JSONParser;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.network.protocol.ServerSettingsRequestPacket;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;

public class Main extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        saveResource("settings.json");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void dataPacket(DataPacketReceiveEvent e) {
        DataPacket pk = e.getPacket();
        if (pk instanceof ServerSettingsRequestPacket) {
            ServerSettingsResponsePacket pk2 = new ServerSettingsResponsePacket();
            pk2.formId = 5928;
            try {
                JSONParser json = new JSONParser();
                Object obj = json.parse(new FileReader(getDataFolder() + "/settings.json"));
                JSONObject settings = (JSONObject) obj;
                pk2.data = settings.toString();
            } catch(Exception ex) {}
            e.getPlayer().dataPacket(pk2);
        }
    }
}
