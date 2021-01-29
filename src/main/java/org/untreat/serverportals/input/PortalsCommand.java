package main.java.org.untreat.serverportals.input;

import main.java.org.untreat.serverportals.Main;
import main.java.org.untreat.serverportals.forms.PortalCreateForm;
import main.java.org.untreat.serverportals.forms.PortalDeleteForm;
import main.java.org.untreat.serverportals.forms.PortalSelectEditForm;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.math.Vector3;

public class PortalsCommand extends Command {
	
	@SuppressWarnings("deprecation")
	public PortalsCommand() {
		super("Portals", "Create a Portal by selecting two points with a stick", "/portals [create/edit/show/hide/delete]");
		this.commandParameters.put("default", new CommandParameter[] {
				new CommandParameter("portalOperation", new String[] {"create", "edit", "show", "hide", "delete"})
		});
		
		this.setPermission("serverportals.use");
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		
		if (!testPermission(sender)) {
			return false;
		}
		
		if (sender instanceof Player && checkArgs(args.length, 1, (Player)sender)) {
			switch (args[0]) {
			case "create":
				Vector3[] playerSelection = new Vector3[2];
				
				try {
					playerSelection = Main.portalOperator.getPlayerSelection((Player)sender);					
				} catch (Exception e) {
					sender.sendMessage("Use a stick to create a selection");
					return true;
				}
				
				try {
					@SuppressWarnings("unused")
					Integer ignore = playerSelection.length;
					((Player)sender).showFormWindow(new PortalCreateForm());
				} catch (Exception e) {
					sender.sendMessage("Finish creating your selection first");
					return true;					
				}
				break;
				
			case "edit":
				((Player)sender).showFormWindow(new PortalSelectEditForm());
				break;
				
			case "delete":
				((Player)sender).showFormWindow(new PortalDeleteForm());
				break;
				
			case "show":
				if (Main.portalOperator.showPortalsToPlayer((Player)sender)) {
					sender.sendMessage("Showing portals for you");						
				} else {
					sender.sendMessage("Portals already shown for you!");						
				}
				break;
				
			case "hide":
				if (Main.portalOperator.hidePortalsFromPlayer((Player)sender)) {
					sender.sendMessage("Hiding portals for you");						
				} else {
					sender.sendMessage("Portals already hidden for you!");
				}
				break;
				
			default:
				sender.sendMessage(this.usageMessage);
				break;
			}
		}
		
		return true;
	}	
	
	private Boolean checkArgs(int argsLength, int correctArgsLength, Player player) {
		if (argsLength > correctArgsLength) {
			player.sendMessage(usageMessage);
			player.sendMessage("Unused args!");
			return false;
		} else if(argsLength < correctArgsLength) {
			player.sendMessage(usageMessage);
			player.sendMessage("Expected more args!");
			return false;
		}
		return true;
	}
}
