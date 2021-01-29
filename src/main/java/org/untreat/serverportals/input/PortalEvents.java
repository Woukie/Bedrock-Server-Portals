package main.java.org.untreat.serverportals.input;

import main.java.org.untreat.serverportals.Main;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class PortalEvents implements Listener {
	//set first position
	@EventHandler
	private void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		if (player.getInventory().getItemInHand().getId() == Item.STICK) {
			Vector3 blockPosVector3 = event.getBlock().getLocation().asVector3f().asVector3();
			
			if (Main.portalOperator.addLeftSelection(blockPosVector3, event.getPlayer())) {
				player.sendMessage("First position placed at " + blockPosVector3.x + ", " + blockPosVector3.y + ", " + blockPosVector3.z);
			} else {
				player.sendMessage("Enexpected Error!");
			}
			event.setCancelled(true);
		}
	}
	
	//set second position
	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getInventory().getItemInHand().getId() == Item.STICK) {
			Vector3 blockPosVector3 = event.getBlock().getLocation().asVector3f().asVector3();
			
			if (Main.portalOperator.addRightSelection(blockPosVector3, event.getPlayer())) {
				player.sendMessage("Second position placed at " + blockPosVector3.x + ", " + blockPosVector3.y + ", " + blockPosVector3.z);
			} else {
				player.sendMessage("Enexpected Error!");
			}
			event.setCancelled(true);
		}		
	}
	
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		Main.portalOperator.checkAndSendPlayer(event.getPlayer());
	}
}
