package net.omniblock.lobbies.main.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.citizensnpcs.api.CitizensAPI;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.api.LobbyUtility;
import net.omniblock.lobbies.api.object.LobbyBoard;
import net.omniblock.lobbies.api.object.LobbyScan;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.api.object.LobbyWorld;
import net.omniblock.lobbies.api.type.CommonLobby;
import net.omniblock.lobbies.apps.attributes.type.AttributeType;
import net.omniblock.lobbies.apps.clicknpc.LobbyNPC;
import net.omniblock.lobbies.apps.general.type.GeneralLobbyItem;
import net.omniblock.lobbies.apps.joinsigns.LobbySigns;
import net.omniblock.lobbies.main.handler.board.MainLobbyBoard;
import net.omniblock.lobbies.main.handler.systems.mjoin.MainJoinSystem;
import net.omniblock.lobbies.utils.PlayerUtils;
import net.omniblock.network.library.addons.resourceaddon.ResourceHandler;
import net.omniblock.network.library.addons.resourceaddon.type.ResourceType;

public class MainLobby extends CommonLobby {

	public static LobbyWorld lobbyWorld = LobbyUtility.getLobbyWorld("Lobby");
	
	protected MainLobby instance;
	protected MainLobbyBoard board;
	
	protected Map<String, List<Location>> scan;
	protected List<LobbySystem> systems;
	
	public MainLobby() {
		super(lobbyWorld);
		return;
		
	}

	@Override
	public void onScanCompleted(Map<String, List<Location>> scan) {
		
		this.scan = scan;
		return;
		
	}

	@Override
	public void onLobbyUnloaded() {
		
	}

	@Override
	public void setup() {
		
		this.start();
		this.instance = this;
		
		LobbySigns.setup();
		LobbyNPC.setup();
		
	}

	@Override
	public void onStartBeingExecute() {
		
		if(systems != null) {
			
			systems.forEach(system -> system.destroy());
			systems = null;
			
		}
		
		this.setSpawnPoint(new Location(this.getWorld().getBukkitWorld(), 0.5, 52, 0.5, -90, (float) 0 ));
		return;
		
	}

	@Override
	public void onStopBeingExecute() {
		
		for(LobbySystem system : getSystems()) {
			
			system.destroy();
			continue;
			
		}
		
		systems = null;
		return;
		
	}

	@Override
	public void giveItems(Player player) {
		
		for(GeneralLobbyItem item : GeneralLobbyItem.values())
			item.addItem(player);
		
	}

	@Override
	public LobbyBoard getBoard() {
		
		if(board != null)
			return board;
		
		return new MainLobbyBoard();
	}

	@Override
	public LobbyScan getScan() {
		return new LobbyScan() {
			@SuppressWarnings("serial")
			@Override
			public Map<String, Material> getKeys() {
				return new HashMap<String, Material>() {{
					
				    put("SKYWARS_TP", Material.BOW);
				    put("COMING_SOON", Material.COAL_BLOCK);
				    
				}};
			}

			@Override
			public String getScanName() {
				return "MAINLOBBY_SCAN";
			}
			
		};
	}

	public Map<String, List<Location>> getLastScan() {
		return scan;
	}
	
	@Override
	public String getLobbyName() {
		return "MainLobby";
	}

	@Override
	public Listener getEvents() {
		return new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			public void onJoin(PlayerJoinEvent e){
				
				PlayerUtils.forcePlayerGameMode(e.getPlayer(), GameMode.ADVENTURE);
				PlayerUtils.clearPlayerInventory(e.getPlayer());
				PlayerUtils.clearPlayerPotions(e.getPlayer());
				
				e.getPlayer().setAllowFlight(false);
				e.getPlayer().setFlying(false);
				
				e.getPlayer().setCanPickupItems(false);
				e.getPlayer().setFireTicks(0);
				
				e.getPlayer().resetMaxHealth();
				e.getPlayer().resetTitle();
				e.getPlayer().resetPlayerWeather();
				e.getPlayer().resetPlayerTime();
				
				e.getPlayer().setExp(0);
				e.getPlayer().setLevel(0);
				
				ResourceHandler.sendResourcePack(e.getPlayer(), ResourceType.OMNIBLOCK_DEFAULT);
				
				giveItems(e.getPlayer());
				teleportPlayer(e.getPlayer());
				
			}
			
		};
	}

	@Override
	public List<LobbySystem> getSystems() {
		
		if(systems != null)
			return systems;
		
		return Arrays.asList(new MainJoinSystem());
	}

	@Override
	public List<BukkitTask> getTasks() {
		return Arrays.asList(
				
				new BukkitRunnable(){

					@Override
					public void run() {
						
						getBoard().sendPacket();
						return;
						
					}
					
				}.runTaskTimer(OmniLobbies.getInstance(), 0L, 5L),
				new BukkitRunnable() {

					@Override
					public void run() {
						
						for(LobbySystem system : getSystems()) {
							
							system.setup(instance);
							system.start();
							continue;
							
						}
						
					}
					
				}.runTaskLater(OmniLobbies.getInstance(), 25L),
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						CitizensAPI.getNPCRegistries().forEach(registry -> {
							
							registry.deregisterAll();
							
						});
						
					}
					
				}.runTaskLater(OmniLobbies.getInstance(), 20L));
	}

	@Override
	public List<AttributeType> getAttributes() {
		return Arrays.asList(
				AttributeType.VOID_TELEPORTER,
				AttributeType.GAMEMODE_ADVENTURE,
				AttributeType.NO_DAMAGE,
				AttributeType.NO_HUNGER,
				AttributeType.NOT_COLLIDE,
				AttributeType.RANK_FLY_ENABLED,
				AttributeType.RANK_JOIN_MSG
				);
	}

}
