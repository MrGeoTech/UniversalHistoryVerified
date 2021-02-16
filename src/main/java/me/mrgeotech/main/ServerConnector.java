package me.mrgeotech.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;

public class ServerConnector implements Runnable {
	
	private final static String IP = "geopixel.ddns.net";
	private final static int PORT = 50000;
	
	private String uuid;
	private ResultSet rs;
	private boolean results;
	
	public ServerConnector() {
		
	}
	
	public ResultSet getPlayerData(String uuid) {
		this.uuid = uuid;
		return rs;
	}
	
	@Override
	public void run() {
		
		try {
			Socket server = new Socket(IP, PORT);
			new ObjectOutputStream(server.getOutputStream()).writeUTF("SELECT * FROM Hisotries WHERE playeruuid='" + uuid + "';");
			ResultSet rs = (ResultSet) new ObjectInputStream(server.getInputStream()).readObject();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
