package me.mrgeotech.main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.bukkit.plugin.java.JavaPlugin;

import me.mrgeotech.commands.UniversalHistoryCommand;

public class UniversalHistory extends JavaPlugin {
	
	@SuppressWarnings("unused")
	private UniversalHistoryCommand uhc;
	
	@Override
	public void onEnable() {
		if (!this.available(50000)) {
			System.err.println("Port 50000 is not open! This plugin will not be able to get any information from the database. Please open port 50000 or you WILL get errors!");
		}
		uhc = new UniversalHistoryCommand(this);
	}
	
	@Override
	public void onDisable() {
		
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
