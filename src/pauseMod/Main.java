package pauseMod;

import arc.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.mod.*;

import static arc.Core.*;
import static mindustry.Vars.net;
import static mindustry.Vars.*;

public class Main extends Mod {
    public Main() {
        Events.on(ClientLoadEvent.class, e ->
            netServer.addPacketHandler("pause", (p, data) -> {
            if (!p.admin) return;
            state.serverPaused ^= true;
        }));

        Events.run(Trigger.update, () -> {
            if (Core.input.keyTap(Binding.pause) && !scene.hasDialog() && !scene.hasKeyboard() && !ui.restart.isShown() && (state.is(GameState.State.paused) || state.is(GameState.State.playing)) && net.active()) {
                if (net.server()) state.serverPaused ^= true;
                else Call.serverPacketReliable("pause", "");
            }
        });
    }
}
