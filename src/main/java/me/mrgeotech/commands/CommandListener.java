package me.mrgeotech.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		String[] sections = e.getMessage().split("");
		switch (sections[0].toLowerCase()) {
		case "kick":
			if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Kick");
			} else if (sections.length == 2) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Kick");
			}
		case "warn":
			if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Warning");
			} else if (sections.length == 2) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Warning");
			}
		case "mute":
			if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Mute");
			} else if (sections.length == 2) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Mute");
			}
		case "tempmute":
			if (sections.length == 4) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Temp Mute", sections[3]);
			} else if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Temp Mute", sections[2]);
			}
		case "ipmute":
			if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "IP Mute");
			} else if (sections.length == 2) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "IP Mute");
			}
		case "ban":
			if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Ban");
			} else if (sections.length == 2) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Ban");
			}
		case "tempban":
			if (sections.length == 4) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Temp Ban", sections[3]);
			} else if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Temp Ban", sections[2]);
			}
		case "ipban":
			if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "IP Ban");
			} else if (sections.length == 2) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "IP Ban");
			}
		case "tempipban":
			if (sections.length == 4) {
				this.sendPunishment(sections[1], e.getPlayer(), sections[2], "Temp IP Ban", sections[3]);
			} else if (sections.length == 3) {
				this.sendPunishment(sections[1], e.getPlayer(), "No reason given", "Temp IP Ban", sections[2]);
			}
		}
	}
	
	private void sendPunishment(final String player, final Player staff, final String reason, final String type) {
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			@Override
			public void run() {
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
					String[] out = {"add", uuid, player, staff.getUniqueId().toString(), staff.getName(), Bukkit.getIp(), ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Chicago")).toString(), type, reason};
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void sendPunishment(final String player, final Player staff, final String reason, final String type, final String length) {
		
	}
	
}
