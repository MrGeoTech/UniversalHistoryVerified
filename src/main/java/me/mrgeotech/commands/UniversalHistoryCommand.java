package me.mrgeotech.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrgeotech.main.BookHandler;
import me.mrgeotech.main.UUIDFetcher;
import me.mrgeotech.main.UniversalHistory;
import net.md_5.bungee.api.ChatColor;

public class UniversalHistoryCommand implements CommandExecutor {
	
	private UniversalHistory main;
	public static HashMap<Player,ItemStack> books;
	public static HashMap<Player,ItemStack> prev;
	
	public UniversalHistoryCommand(UniversalHistory main) {
		this.main = main;
		this.main.getCommand("uh").setExecutor(this);
		books = new HashMap<Player,ItemStack>();
		prev = new HashMap<Player,ItemStack>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("uh.admin.uh")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cI'm sorry but you don't have sufficient permissions to execute this command. If you think that this is an error, contact the server administrator."));
			return true;
		}
		if (args.length != 2) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Improper usage! /uh <check/add> <name>"));
			return true;
		}
		String type = args[0];
		String player = args[1];
		if (type.equalsIgnoreCase("add")) {
			sender.sendMessage(ChatColor.RED + "Sorry but you do not have the " + ChatColor.DARK_RED + "TRUSTED" + ChatColor.RED + "version of this plugin!");
			sender.sendMessage(ChatColor.RED + "If you would like to get verified, join the discord at \"" + ChatColor.AQUA + "https://discord.gg/QWt4SsBBWV\"" + ChatColor.RED + " to become verified.");
			return true;
		}
		if (type.equalsIgnoreCase("check")) {
			try {
				ResultSet rs = this.main.getServerConnector().getPlayerData(UUIDFetcher.getUUIDOf(player).toString());
				ArrayList<String> playerUUID = new ArrayList<String>();
				ArrayList<String> playerName = new ArrayList<String>();
				ArrayList<String> staffUUID = new ArrayList<String>();
				ArrayList<String> staffName = new ArrayList<String>();
				ArrayList<String> serverIP = new ArrayList<String>();
				ArrayList<String> date = new ArrayList<String>();
				ArrayList<String> ptype = new ArrayList<String>();
				ArrayList<String> reason = new ArrayList<String>();stop
				while (rs.next()) {
					playerUUID.add(rs.getString("PlayerUUID"));
					playerName.add(rs.getString("PlayerName"));
					staffUUID.add(rs.getString("StaffUUID"));
					staffName.add(rs.getString("StaffName"));
					serverIP.add(rs.getString("ServerIP"));
					date.add(rs.getString("Date"));
					ptype.add(rs.getString("Type"));
					reason.add(rs.getString("Reason"));
				}
				if (sender instanceof Player) {
					ItemStack book = new BookHandler(playerUUID, playerName, staffUUID, staffName, serverIP, date, ptype, reason).buildBook();
					Player staff = (Player) sender;
					prev.put(staff, staff.getInventory().getItemInMainHand());
					books.put(staff, book);
				} else {
					
				}
			} catch (NullPointerException e) {
				sender.sendMessage(ChatColor.RED + "An error occured getting the UUID of the player. Please make sure that the player is online.");
				e.printStackTrace();
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
}
