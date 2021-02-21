package me.mrgeotech.main;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.ChatColor;

public class BookHandler {
	
	private String player;
	private ArrayList<String> playerUUID;
	private ArrayList<String> playerName;
	private ArrayList<String> staffUUID;
	private ArrayList<String> staffName;
	private ArrayList<String> serverIP;
	private ArrayList<String> date;
	private ArrayList<String> type;
	private ArrayList<String> reason;
	
	public BookHandler(String player, ArrayList<String> playerUUID, ArrayList<String> playerName, ArrayList<String> staffUUID, ArrayList<String> staffName, ArrayList<String> serverIP, ArrayList<String> date, ArrayList<String> type, ArrayList<String> reason) {
		this.player = player;
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.staffUUID = staffUUID;
		this.staffName = staffName;
		this.serverIP = serverIP;
		this.date = date;
		this.type = type;
		this.reason = reason;
	}
	
	public ItemStack buildBook() {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		
		meta.addPage(color("&0This is the punishment history of &b" + this.playerName.get(0) + "&0. Each page represents a punishment they have recived on other servers."));;
		meta.setAuthor("UniversalHistory");
		meta.setDisplayName(playerName.get(0) + "'s History");
		meta.setTitle(playerName.get(0) + "'s History");
		if (playerUUID.size() != 0) {
			for (int i = 0; playerUUID.size() > i; i++) {
				if (meta.getPageCount() <= 49) {
					String punishment = color("&5" + playerName.get(i) + "&0(&d" + playerUUID.get(i) + "&0) has a &c" + type.get(i) + "&0 on &5" + serverIP.get(i) + "&0, given by &5" + staffName.get(i) + "&0(&d" + staffUUID.get(i) + "&0) for \"&a" + reason.get(i) + "&0\" at &2" + date.get(i));
					if (punishment.length() > 256) {
						String punishmentTwo = punishment.substring(255);
						meta.addPage(punishment);
						meta.addPage(punishmentTwo);
					} else {
						meta.addPage(punishment);
					}
				} else {
					meta.addPage(ChatColor.DARK_RED + "Unfortuatly, this player has more punishments that can be displayed with the current book system. Please use either the console or contact MrGeoTech#9470 for the rest of the punishments.");
				}
			}
		} else {
			meta.addPage(ChatColor.BLACK + "There are not recorded punishments for " + player + ".");
		}
		book.setItemMeta(meta);
		return book;
	}
	
	private String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
}
