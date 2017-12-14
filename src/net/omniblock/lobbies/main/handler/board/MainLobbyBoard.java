package net.omniblock.lobbies.main.handler.board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.omniblock.lobbies.api.object.LobbyBoard;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.library.helpers.scoreboard.ScoreboardUtil;
import net.omniblock.network.library.utils.TextUtil;

public class MainLobbyBoard implements LobbyBoard {

	protected static String title = TextUtil.format("&6&l  Omniblock Network  ");
	
	protected static int title_round = 0;
	protected static int title_pos = 0;
	
	@Override
	public void sendPacket(boolean condition){
		return;
	}
	
	@Override
	public void sendPacket() {
		
		for(Player player : Bukkit.getOnlinePlayers()){
			
			ScoreboardUtil.unrankedSidebarDisplay(
					player, 
					new String[] { 
								   title,
								   TextUtil.format(" "),
								   TextUtil.format("&7OmniCoins: &b" + BankBase.getMoney(player)),
								   TextUtil.format("&7Nivel: &b" + BankBase.getLevel(player)),
								   TextUtil.format("   "),
								   TextUtil.format("&7Fase: &c&lBETA"),
								   TextUtil.format("&7Tu Key: &a#000fff"),
								   TextUtil.format("    "),
								   TextUtil.format("&ewww.omniblock.net")}, false);
			
		}
		
		return;
		
	}
	
}
