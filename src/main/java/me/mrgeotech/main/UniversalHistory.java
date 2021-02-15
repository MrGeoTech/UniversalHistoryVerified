package me.mrgeotech.main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class UniversalHistory extends JavaPlugin {
	
	private ServerConnector sc;
	
	@Override
	public void onEnable() {
		if (!this.available(50000)) {
			this.getServer().broadcastMessage(ChatColor.RED + "Could not enable plugin \"UniversalHistory\" because port 50000 is either in use or closed!");
			this.getServer().getPluginManager().disablePlugin(this);
		}
		sc = new ServerConnector();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public ServerConnector getServerConnector() {
		return sc;
	}
	
	public boolean available(int port) {
	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
	
}
