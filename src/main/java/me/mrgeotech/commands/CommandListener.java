package me.mrgeotech.commands;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import me.mrgeotech.main.UniversalHistory;
import net.md_5.bungee.api.ChatColor;

public class CommandListener implements Listener {
	
	private final String[] commands = {"warn", "kick", "mute", "ban", "ipban", "banip"};
	private final String[] tempCommands = {"tempmute", "tempban", "tempipban", "tempbanip"};
	
	private UniversalHistory main;
	
	public CommandListener(UniversalHistory main) {
		this.main = main;
		this.main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onServerCommand(ServerCommandEvent e) {
		ArrayList<String> sections = new ArrayList<String>();
		for (String s : e.getCommand().split(" ")) {
			sections.add(s);
		}
		String command = sections.get(0);
		sections.remove(0);
		if ((sections.size() < 1 && isPunishment(command)) || (sections.size() < 2 && isTempPunishment(command)) || !isPunishment(command)) return;
		if (sections.get(0).equalsIgnoreCase("-s")) {
			sections.remove(0);
		}
		String player = sections.get(0);
		sections.remove(0);
		String length = "";
		if (isTempPunishment(command)) {
			length = sections.get(0);
			sections.remove(0);
		}
		String reason = "";
		try {
			reason += sections.get(0);
			sections.remove(0);
			for (String s : sections) {
				reason += " " + s;
			}
		} catch (Exception ex) {
			reason = "No reason given";
		}
		if (reason.length() >= 255) {
			e.getSender().sendMessage(ChatColor.DARK_RED + "The length of the reason is too long for our database! Please fix this and then use the /uh add command to enter this into the database.");
			return;
		}
		this.main.getPunishmentHandler().sendPunishment(player, "Console", "Console", reason, command, length);
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		ArrayList<String> sections = new ArrayList<String>();
		for (String s : e.getMessage().split(" ")) {
			sections.add(s);
		}
		String command = sections.get(0).substring(1);
		sections.remove(0);
		if ((sections.size() < 1 && isPunishment(command)) || (sections.size() < 2 && isTempPunishment(command)) || !isPunishment(command)) return;
		if (sections.get(0).equalsIgnoreCase("-s")) {
			sections.remove(0);
		}
		String player = sections.get(0);
		sections.remove(0);
		String length = "";
		if (isTempPunishment(command)) {
			length = sections.get(0);
			sections.remove(0);
		}
		String reason = "";
		try {
			reason += sections.get(0);
			sections.remove(0);
			for (String s : sections) {
				reason += " " + s;
			}
		} catch (Exception ex) {
			reason = "No reason given";
		}
		if (reason.length() >= 255) {
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "The length of the reason is too long for our database! Please fix this and then use the /uh add command to enter this into the database.");
			return;
		}
		this.main.getPunishmentHandler().sendPunishment(player, e.getPlayer().getUniqueId().toString(), e.getPlayer().getName(), reason, command, length);
	}
	
	private boolean isPunishment(String command) {
		boolean isCommand = false;
		for (String s : commands) {
			if (s.equalsIgnoreCase(command)) {
				isCommand = true;
			}
		}
		for (String s : tempCommands) {
			if (s.equalsIgnoreCase(command)) {
				isCommand = true;
			}
		}
		return isCommand;
	}
	
	private boolean isTempPunishment(String command) {
		boolean isCommand = false;
		for (String s : tempCommands) {
			if (s.equalsIgnoreCase(command)) {
				isCommand = true;
			}
		}
		return isCommand;
	}
	
}
