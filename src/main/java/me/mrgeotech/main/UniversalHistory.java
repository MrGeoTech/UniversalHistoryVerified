package me.mrgeotech.main;

import org.bukkit.plugin.java.JavaPlugin;

import me.mrgeotech.commands.CommandListener;
import me.mrgeotech.commands.PunishmentHandler;
import me.mrgeotech.commands.UniversalHistoryCommand;

public class UniversalHistory extends JavaPlugin {
	
	@SuppressWarnings("unused")
	private UniversalHistoryCommand uhc;
	private PunishmentHandler ph;
	
	@Override
	public void onEnable() {
		uhc = new UniversalHistoryCommand(this);
		ph = new PunishmentHandler(this);
		@SuppressWarnings("unused")
		CommandListener cl = new CommandListener(this);
	}
	
	@Override
	public void onDisable() {
		
	}

	public PunishmentHandler getPunishmentHandler() {
		return ph;
	}
	
}
