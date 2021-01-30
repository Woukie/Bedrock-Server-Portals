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
	// Set first position
	@EventHandler
	private void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		if (player.getInventory().getItemInHand().getId() == Item.STICK && player.hasPermission("serverportals.use")) {
			Vector3 blockPosVector3 = event.getBlock().getLocation().asVector3f().asVector3();
			
			if (Main.portalOperator.addLeftSelection(blockPosVector3, player)) {
				player.sendMessage("First position placed at " + blockPosVector3.x + ", " + blockPosVector3.y + ", " + blockPosVector3.z);
			}
			
			event.setCancelled(true);
		}
	}
	
	// Set second position
	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getInventory().getItemInHand().getId() == Item.STICK && player.hasPermission("serverportals.use")) {
			Vector3 blockPosVector3 = event.getBlock().getLocation().asVector3f().asVector3();
			
			if (Main.portalOperator.addRightSelection(blockPosVector3, player)) {
				player.sendMessage("Second position placed at " + blockPosVector3.x + ", " + blockPosVector3.y + ", " + blockPosVector3.z);
			}
			
			event.setCancelled(true);
		}		
	}
	
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		Main.portalOperator.checkAndSendPlayer(event.getPlayer());
	}
}
