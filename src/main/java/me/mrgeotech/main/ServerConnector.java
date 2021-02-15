package me.mrgeotech.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;

public class ServerConnector {
	
	private final static String IP = "geopixel.ddns.net";
	private final static int PORT = 50000;
	
	public ServerConnector() {
		
	}
	
	public ResultSet getPlayerData(String uuid) {
		try {
			Socket server = new Socket(IP, PORT);
			new ObjectOutputStream(server.getOutputStream()).writeUTF("SELECT * FROM Hisotries WHERE playeruuid='" + uuid + "';");
			ResultSet rs = (ResultSet) new ObjectInputStream(server.getInputStream()).readObject();
			server.close();
			return rs;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
