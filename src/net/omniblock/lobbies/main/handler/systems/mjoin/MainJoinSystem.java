package net.omniblock.lobbies.main.handler.systems.mjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import net.citizensnpcs.api.npc.NPC;
import net.omniblock.lobbies.api.Lobby;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.main.handler.MainLobby;
import net.omniblock.lobbies.main.handler.systems.mjoin.type.MainJoinType;

public class MainJoinSystem implements LobbySystem {

	protected List<Entry<NPC, Hologram>> displayedEntries = new ArrayList<Entry<NPC, Hologram>>();
	protected MainLobby lobby;
	
	@Override
	public void setup(Lobby lobby) {
		
		if(lobby instanceof MainLobby) {
			
			this.lobby = (MainLobby) lobby;
			
		}
		
	}

	@Override
	public void start() {
		
		if(this.lobby == null)
			return;
		
		lobby.getLastScan().get("SKYWARS_TP").forEach(location -> displayedEntries.add(MainJoinType.SKYWARS.spawnJoin(location)));
		lobby.getLastScan().get("SURVIVAL_TP").forEach(location -> displayedEntries.add(MainJoinType.SURVIVAL.spawnJoin(location)));
		lobby.getLastScan().get("COMING_SOON").forEach(location -> displayedEntries.add(MainJoinType.COMING_SOON.spawnJoin(location)));
		
	}

	@Override
	public void destroy() {
		
		displayedEntries.forEach(entry -> {
			entry.getKey().destroy();
			entry.getValue().delete();
		});
		
		displayedEntries.clear();
		
	}

}
