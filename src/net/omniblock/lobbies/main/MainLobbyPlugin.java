package net.omniblock.lobbies.main;

import org.bukkit.plugin.java.JavaPlugin;

import net.omniblock.lobbies.api.LobbyHandler;
import net.omniblock.lobbies.main.handler.MainLobby;
import net.omniblock.network.handlers.Handlers;
import net.omniblock.network.handlers.network.NetworkManager;
import net.omniblock.packets.object.external.ServerType;

public class MainLobbyPlugin extends JavaPlugin {

	private static MainLobbyPlugin instance;
	private static MainLobby lobby;
	
	@Override
	public void onEnable() {
		
		instance = this;
		lobby = new MainLobby();
		
		if(NetworkManager.getServertype() != ServerType.MAIN_LOBBY_SERVER) {
			
			Handlers.LOGGER.sendModuleInfo("&7Se ha registrado MainLobby v" + this.getDescription().getVersion() + "!");
			Handlers.LOGGER.sendModuleMessage("OmniLobbies", "Se ha inicializado MainLobby en modo API!");
			return;
			
		}
		
		Handlers.LOGGER.sendModuleInfo("&7Se ha registrado MainLobby v" + this.getDescription().getVersion() + "!");
		Handlers.LOGGER.sendModuleMessage("OmniLobbies", "Se ha inicializado este lobby como un MainLobby!");
		
		LobbyHandler.startLobby(lobby);
		
	}
	
	public MainLobbyPlugin getInstance() {
		return instance;
	}
	
}

