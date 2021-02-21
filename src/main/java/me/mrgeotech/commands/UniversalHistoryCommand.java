package me.mrgeotech.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.bukkit.Bukkit;
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
	
	private static final String IP = "geopixel.ddns.net";
	private static final int PORT = 50000;
	
	private UniversalHistory main;
	
	public UniversalHistoryCommand(UniversalHistory main) {
		this.main = main;
		this.main.getCommand("uh").setExecutor(this);
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("uh.admin.uh")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cI'm sorry but you don't have sufficient permissions to execute this command. If you think that this is an error, contact the server administrator."));
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Improper usage! /uh <check/add>");
			return true;
		}
		String type = args[0];
		if (type.equalsIgnoreCase("check")) {
			if (args.length != 2) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Improper usage! /uh check <name>"));
				return true;
			}
			String player = args[1];
			Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
				@Override
				public void run() {
					try {
						String uuid;
						try {
							uuid = UUIDFetcher.getUUIDOf(player).toString();
						} catch (Exception e) {
							System.err.println(sender.getName() + " tried to get the uuid of " + player + " which could not be found/doesn't exist!");
							sender.sendMessage(ChatColor.RED + player + "'s uuid could not be found/doesn't exist!");
							return;
						}
						Socket server = new Socket(IP, PORT);
						ObjectOutputStream outStream = new ObjectOutputStream(server.getOutputStream());
						ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
						String[] out = new String[2];
						out[0] = "get";
						out[1] = uuid;
						outStream.writeObject(out);
						@SuppressWarnings("unchecked")
						ArrayList<String> in = (ArrayList<String>) inStream.readObject();
						server.close();
						ArrayList<String> playerUUID = new ArrayList<String>();
						ArrayList<String> playerName = new ArrayList<String>();
						ArrayList<String> staffUUID = new ArrayList<String>();
						ArrayList<String> staffName = new ArrayList<String>();
						ArrayList<String> serverIP = new ArrayList<String>();
						ArrayList<String> date = new ArrayList<String>();
						ArrayList<String> ptype = new ArrayList<String>();
						ArrayList<String> reason = new ArrayList<String>();
						for (int i = 0; (in.size() / 8) > i; i++) {
							playerUUID.add(in.get(i * 8));
							playerName.add(in.get((i * 8) + 1));
							staffUUID.add(in.get((i * 8) + 2));
							staffName.add(in.get((i * 8) + 3));
							serverIP.add(in.get((i * 8) + 4));
							date.add(in.get((i * 8) + 5));
							ptype.add(in.get((i * 8) + 6));
							reason.add(in.get((i * 8) + 7));
						}
						if (sender instanceof Player) {
							if (playerUUID.size() != 0) {
								ItemStack book = new BookHandler(player, playerUUID, playerName, staffUUID, staffName, serverIP, date, ptype, reason).buildBook();
								Player staff = (Player) sender;
								if (!staff.getInventory().addItem(book).isEmpty()) {
									staff.getInventory().setItem(8, book);
								}
							} else {
								sender.sendMessage(ChatColor.RED + "There was no history for " + player + " in our database.");
							}
						} else {
							if (playerUUID.size() != 0) {
								for (int i = 0; playerUUID.size() > i; i++) {
									System.out.println(ChatColor.translateAlternateColorCodes('&', "&5" + playerName.get(i) + "&f(&d" + playerUUID.get(i) + "&f) has a &c" + ptype.get(i) + "&f on &5" + serverIP.get(i) + "&f, given by &5" + staffName.get(i) + "&f(&d" + staffUUID.get(i) + "&f) for \"&a" + reason.get(i) + "&f\" at &2" + date.get(i)));
								}
							} else {
								System.out.println(ChatColor.RED + "There was no history for " + player + " in our database.");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
			return true;
		}
		if (type.equalsIgnoreCase("add")) {
			if (args.length < 6) {
				sender.sendMessage(ChatColor.DARK_RED + "Improper usage! /uh add <playerName> <staffName> <punishmentType> <length> <reason>");
				sender.sendMessage(ChatColor.DARK_RED + "If the punishment does not have a length, then enter \"null\" into the length area.");
				return true;
			}
			ArrayList<String> sections = new ArrayList<String>();
			for (String s : args) {
				sections.add(s);
			}
			sections.remove(0);
			sections.remove(0);
			sections.remove(0);
			sections.remove(0);
			sections.remove(0);
			String player = args[1];
			String staff = args[2];
			String punishment = args[3];
			String length = args[4];
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
				sender.sendMessage(ChatColor.DARK_RED + "The length of the reason is too long for our database! Please fix this and then try again.");
				return true;
			}
			if (staff.equalsIgnoreCase("console")) {
				this.main.getPunishmentHandler().sendPunishment(player, staff, staff, reason, punishment, length);
			} else {
				this.main.getPunishmentHandler().sendPunishment(player, staff, reason, punishment, length);
			}
		}
		return false;
	}
}
