package me.mrgeotech.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	private static final String IP = "localhost";
	private static final int PORT = 50000;
	
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
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
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
			Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					try {
						String uuid = UUIDFetcher.getUUIDOf(player).toString();
						System.out.println(uuid);
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
							playerUUID.add(in.get(i));
							playerName.add(in.get(i + 1));
							staffUUID.add(in.get(i + 2));
							staffName.add(in.get(i + 3));
							serverIP.add(in.get(i + 4));
							date.add(in.get(i + 5));
							ptype.add(in.get(i + 6));
							reason.add(in.get(i + 7));
						}
						if (sender instanceof Player) {
							if (playerUUID.size() != 0) {
								ItemStack book = new BookHandler(player, playerUUID, playerName, staffUUID, staffName, serverIP, date, ptype, reason).buildBook();
								Player staff = (Player) sender;
								if (Bukkit.getBukkitVersion().split("-")[0].equalsIgnoreCase("1.8")) {
									prev.put(staff, staff.getItemInHand());
								} else {
									prev.put(staff, staff.getInventory().getItemInMainHand());
								}
								books.put(staff, book);
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
		return false;
	}
}
