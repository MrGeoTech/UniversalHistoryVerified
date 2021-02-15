package me.mrgeotech.commands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class HandEvent {
	
	@EventHandler
	public void onChangeItemHeld(PlayerItemHeldEvent e) {
		if (UniversalHistoryCommand.prev.get(e.getPlayer()) == null) return;
		if (!e.getPlayer().getInventory().contains(UniversalHistoryCommand.books.get(e.getPlayer()))) return;
		
		e.getPlayer().getInventory().setItem(e.getPreviousSlot(), UniversalHistoryCommand.prev.get(e.getPlayer()));
	}
	
}
