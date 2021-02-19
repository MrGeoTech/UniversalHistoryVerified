package me.mrgeotech.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import me.mrgeotech.main.UUIDFetcher;
import me.mrgeotech.main.UniversalHistory;
import net.md_5.bungee.api.ChatColor;

public class CommandListener implements Listener {
	
	private static final String IP = "geopixel.ddns.net";
	private static final int PORT = 50000;
	
	private UniversalHistory main;
	
	public CommandListener(UniversalHistory main) {
		this.main = main;
		this.main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void commandListener(ServerCommandEvent e) {
		ArrayList<String> sections = new ArrayList<String>();
		for (String s : e.getCommand().split(" ")) {
			sections.add(s);
		}
		System.out.println(sections);
		String command = sections.get(0);
		if (sections.get(1).equalsIgnoreCase("-s")) {
			System.out.println("True");
			sections.remove(1);
		}
		String playerName = sections.get(1);
		sections.remove(0);
		sections.remove(0);
		System.out.println(sections);
		String reason = sections.get(0);
		sections.remove(0);
		for (String s : sections) {
			reason.concat(" " + s);
		}
		System.out.println(command + " : " + playerName + " : " + reason);
	}

	public void sendPunishment(final String player, final CommandSender sender, final String reason, final String type, final String length) {
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			@Override
			public void run() {
				if (sender instanceof Player) {
					Player staff = (Player) sender;
					try {
						String uuid;
						try {
							uuid = UUIDFetcher.getUUIDOf(player).toString();
						} catch (Exception e) {
							System.err.println(staff.getName() + " tried to get the uuid of " + player + " which could not be found/doesn't exist!");
							staff.sendMessage(ChatColor.RED + player + "'s uuid could not be found/doesn't exist!");
							return;
						}
						Socket server = new Socket(IP, PORT);
						ObjectOutputStream outStream = new ObjectOutputStream(server.getOutputStream());
						ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
						String[] out = {"add", uuid, player, staff.getUniqueId().toString(), staff.getName(), Bukkit.getIp(), ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Chicago")).toString(), type + "(Length=" + length + ")", reason};
						outStream.writeObject(out);
						String response = inStream.readUTF();
						staff.sendMessage(response);
						server.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						String uuid;
						try {
							uuid = UUIDFetcher.getUUIDOf(player).toString();
						} catch (Exception e) {
							System.err.println("Console tried to get the uuid of " + player + " which could not be found/doesn't exist!");
							sender.sendMessage(ChatColor.RED + player + "'s uuid could not be found/doesn't exist!");
							return;
						}
						Socket server = new Socket(IP, PORT);
						ObjectOutputStream outStream = new ObjectOutputStream(server.getOutputStream());
						ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
						String[] out = {"add", uuid, player, "Console", "Console", Bukkit.getIp(), ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Chicago")).toString(), type + "(Length=" + length + ")", reason};
						outStream.writeObject(out);
						String response = inStream.readUTF();
						sender.sendMessage(response);
						server.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
