package net.omniblock.lobbies.main.handler.systems.mjoin.type;

import java.util.AbstractMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.apps.clicknpc.LobbyNPC;
import net.omniblock.lobbies.apps.clicknpc.object.NPCActioner;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;

public enum MainJoinType {

	SKYWARS("&a&lSKYWARS", "ColdCrawL", ServerType.SKYWARS_LOBBY_SERVER, 90, -1),
	COMING_SOON("&c&lPRÓXIMAMENTE", "Im_a_Stone", null, 90, -1),
	
	;
	
	private String modeName;
	private String modeSkin;
	
	private ServerType type;
	private float yaw, pitch;
	
	MainJoinType(String modeName, String modeSkin, ServerType type, float yaw, float pitch) {
		
		this.modeName = modeName;
		this.modeSkin = modeSkin;
		
		this.type = type;
		
		this.yaw = yaw;
		this.pitch = pitch;
		
	}
	
	public Entry<NPC, Hologram> spawnJoin(Location loc){
		
		Location faceLoc = loc.clone();
		
		faceLoc.setYaw(yaw);
		faceLoc.setPitch(pitch);
		
		if(this == COMING_SOON) {
			
			NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, modeSkin);
			Hologram hologram = HologramsAPI.createHologram(OmniLobbies.getInstance(), loc.clone().add(0, 2.5, 0));
			
			npc.spawn(faceLoc);
			npc.setName(TextUtil.format(modeName));
			npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, modeSkin);
			
			LobbyNPC.registerActioner(npc, new NPCActioner() {

				@Override
				public void execute(NPC npc, Player player) {
					
					player.sendMessage(TextUtil.format("&6Proximamente..."));
					return;
					
				}
				
			});
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					if(!npc.isSpawned()) {
						
						this.cancel();
						return;
						
					}
					
					npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, modeSkin);
					return;
					
				}
				
			}.runTaskTimer(OmniLobbies.getInstance(), 80L, 20 * 120);
			
			return new AbstractMap.SimpleEntry<NPC, Hologram>(npc, hologram);
			
		}
		
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, modeSkin);
		Hologram hologram = HologramsAPI.createHologram(OmniLobbies.getInstance(), loc.clone().add(0, 2.7, 0));
		
		hologram.appendTextLine(TextUtil.format(modeName));
		
		npc.spawn(faceLoc);
		npc.setName(TextUtil.format("&7(Entrar)"));
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, modeSkin);
		
		LobbyNPC.registerActioner(npc, new NPCActioner() {

			@Override
			public void execute(NPC npc, Player player) {
				
				player.sendMessage(TextUtil.format("&bConectandote a Skywars..."));
				
				Packets.STREAMER.streamPacket(new PlayerSendToServerPacket()
						.setPlayername(player.getName())
						.setServertype(type)
						.setParty(false)
						.build().setReceiver(PacketSenderType.OMNICORE));
				return;
				
			}
			
		});
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(!npc.isSpawned()) {
					
					this.cancel();
					return;
					
				}
				
				npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, modeSkin);
				return;
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 80L, 20 * 120);
		
		return new AbstractMap.SimpleEntry<NPC, Hologram>(npc, hologram);
		
	}
	
}
