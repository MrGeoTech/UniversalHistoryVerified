package me.mrgeotech.commands;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.mrgeotech.main.UUIDFetcher;
import me.mrgeotech.main.UniversalHistory;

public class PunishmentHandler {
	
	private static final String IP = "geopixel.ddns.net";
	private static final int PORT = 50000;
	
	private static UniversalHistory main;
	
	public PunishmentHandler(UniversalHistory plugin) {
		main = plugin;
	}
	
	public void sendPunishment(final String playerUUID, final String playerName, final String senderUUID, final String senderName, final String reason, final String type, final String length) {
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			@Override
			public void run() {
				try {
					Socket server = new Socket(IP, PORT);
					ObjectOutputStream outStream = new ObjectOutputStream(server.getOutputStream());
					ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
					String[] out = {"add", playerUUID, playerName, senderUUID, senderName, ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Chicago")).toString(), (length.equalsIgnoreCase("null")) ? type : type + "(Length=" + length + ")", reason};
					outStream.writeObject(out);
					boolean response;
					try {
						response = inStream.readBoolean();
					} catch (EOFException e) {
						response = true;
					}
					try {
						if (response) {
							Bukkit.getPlayer(senderName).sendMessage(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						} else {
							Bukkit.getPlayer(senderName).sendMessage(ChatColor.DARK_GREEN + "I'm sorry but it appears that your ip has not been approved. If you think that this is a mistake, please contact MrGeoTech on discord at MrGeoTech#9470");
						}
					} catch (Exception e) {
						if (response) {
							System.out.println(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						} else {
							System.out.println(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						}
					}
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void sendPunishment(final String playerName, final String senderUUID, final String senderName, final String reason, final String type, final String length) {
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			@Override
			public void run() {
				try {
					String playerUUID = UUIDFetcher.getUUIDOf(playerName).toString();
					Socket server = new Socket(IP, PORT);
					ObjectOutputStream outStream = new ObjectOutputStream(server.getOutputStream());
					ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
					String[] out = {"add", playerUUID, playerName, senderUUID, senderName, ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Chicago")).toString(), (length.equalsIgnoreCase("null")) ? type : type + "(Length=" + length + ")", reason};
					outStream.writeObject(out);
					boolean response;
					try {
						response = inStream.readBoolean();
					} catch (EOFException e) {
						response = true;
					}
					try {
						if (response) {
							Bukkit.getPlayer(senderName).sendMessage(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						} else {
							Bukkit.getPlayer(senderName).sendMessage(ChatColor.DARK_GREEN + "I'm sorry but it appears that your ip has not been approved. If you think that this is a mistake, please contact MrGeoTech on discord at MrGeoTech#9470");
						}
					} catch (Exception e) {
						if (response) {
							System.out.println(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						} else {
							System.out.println(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						}
					}
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void sendPunishment(final String playerName, final String senderName, final String reason, final String type, final String length) {
		Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
			@Override
			public void run() {
				try {
					String playerUUID = UUIDFetcher.getUUIDOf(playerName).toString();
					String senderUUID = UUIDFetcher.getUUIDOf(senderName).toString();
					Socket server = new Socket(IP, PORT);
					ObjectOutputStream outStream = new ObjectOutputStream(server.getOutputStream());
					ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
					String[] out = {"add", playerUUID, playerName, senderUUID, senderName, ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Chicago")).toString(), (length.equalsIgnoreCase("null")) ? type : type + "(Length=" + length + ")", reason};
					outStream.writeObject(out);
					boolean response;
					try {
						response = inStream.readBoolean();
					} catch (EOFException e) {
						response = true;
					}
					try {
						if (response) {
							Bukkit.getPlayer(senderName).sendMessage(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						} else {
							Bukkit.getPlayer(senderName).sendMessage(ChatColor.DARK_GREEN + "I'm sorry but it appears that your ip has not been approved. If you think that this is a mistake, please contact MrGeoTech on discord at MrGeoTech#9470");
						}
					} catch (Exception e) {
						if (response) {
							System.out.println(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						} else {
							System.out.println(ChatColor.DARK_GREEN + "History has been added to database! Thank you for your contibution!");
						}
					}
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
