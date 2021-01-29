package main.java.org.untreat.serverportals.forms;

import main.java.org.untreat.serverportals.Main;

import com.google.common.net.InetAddresses;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.math.Vector3;

public class FormHandler implements Listener {
	
	@EventHandler
	private void formComplete(PlayerFormRespondedEvent event) {
		if (event.getWindow() instanceof FormWindowCustom && !event.wasClosed()) {
			FormResponseCustom formResponse = (FormResponseCustom)event.getResponse();
			FormWindowCustom formWindow = (FormWindowCustom)event.getWindow();
			Player player = event.getPlayer();
			
			// Confirmation form for replacing a portal and filling it with another portals data
			if (formWindow.getTitle().startsWith("Server Portals: Replace Portal ")) {
				String[] formData = formResponse.getLabelResponse(0).split("\n");
				
				String fromPortalName = formData[0];
				String toPortalName = formWindow.getTitle().replace("Server Portals: Replace Portal ", "").replace("?", "");
				
				String IPAddress = formData[1];
				Integer port = Integer.parseInt(formData[2]);
				String[] stringCorner0 = formData[3].replaceAll("[()=xyz]", "").split(",");
				String[] stringCorner1 = formData[4].replaceAll("[()=xyz]", "").split(",");
				
				Vector3 corner0 = new Vector3(Double.parseDouble(stringCorner0[0]), Double.parseDouble(stringCorner0[1]), Double.parseDouble(stringCorner0[2]));
				Vector3 corner1 = new Vector3(Double.parseDouble(stringCorner1[0]), Double.parseDouble(stringCorner1[1]), Double.parseDouble(stringCorner1[2]));
				Vector3[] corners = {corner0, corner1};
				
				Main.portalOperator.deletePortal(fromPortalName);
				Main.portalOperator.createPortal(IPAddress, port, corners, toPortalName);
			}
			
			if (formWindow.getTitle().equals("Server Portals: Delete Portal")) {
				String portalName = formResponse.getDropdownResponse(1).getElementContent();
				
				Main.portalOperator.deletePortal(portalName);
				
				player.sendMessage("Successfully deleted portal " + portalName + ".");
			}
			
			if (formWindow.getTitle().equals("Server Portals: Create Portal")) {
				String IPAddress = formResponse.getInputResponse(1);
				String portalName = formResponse.getInputResponse(0);
				Integer port = 0;
				Vector3[] corners;
				
				// Check port
				try {
					port = Integer.parseInt(formResponse.getInputResponse(2));
					if (port < 1024 || port > 65535) {
						player.sendMessage("Port must be in the correct format!");
						return;
					}
					
				} catch (Exception e) {
					player.sendMessage("Port must be an integer!");
					return;
				}
				
				// Check selection
				try {
					corners = Main.portalOperator.getPlayerSelection(player);
					
				} catch (Exception e) {
					player.sendMessage("You must make a selection first!");
					return;
				}
				
				// Check IP address
				if (!InetAddresses.isInetAddress(IPAddress)) {
					player.sendMessage("IP address must be in the correct format!");
					return;
				}
				
				try {
					if (Main.portalOperator.doesPortalExist(portalName)) {
						player.showFormWindow(new PortalReplace(portalName, IPAddress, port.toString(), corners[0].toString(), corners[1].toString(), portalName));
						return;
					}
					
					Main.portalOperator.createPortal(IPAddress, port, corners, portalName);
					player.sendMessage("Created portal with name " + portalName);
				} catch (Exception e) {
					// TODO: This might cause the portals stored in game and in the json to be out of sync
					player.sendMessage("Unexpected Error!");
				}
				
			}
			
			// For selecting a portal to edit
			if (formWindow.getTitle().equals("Server Portals: Edit Portal")) {
				player.showFormWindow(new PortalEditForm(formResponse.getDropdownResponse(0).getElementContent()));
			}
			
			// For specific portal
			if (formWindow.getTitle().startsWith("Server Portals: Edit Portal ")) {
				String oldPortalName = formWindow.getTitle().replaceFirst("Server Portals: Edit Portal ", "");
				String newPortalName = formResponse.getInputResponse(0);
				
				String possibleNewIPAddress = formResponse.getInputResponse(1);
				Integer possibleNewPort = 0;
				
				try {
					possibleNewPort = Integer.parseInt(formResponse.getInputResponse(2));
					if (possibleNewPort < 1024 || possibleNewPort > 65535) {
						player.sendMessage("Port must be in the correct format!");
						return;
					}
				} catch (Exception e) {
					player.sendMessage("Port must be an integer!");
					return;
				}
				
				if (!InetAddresses.isInetAddress(possibleNewIPAddress)) {
					player.sendMessage("IP address must be in the correct format!");
					return;
				}
				
				if (Main.portalOperator.getPortalNames().contains(newPortalName) && !newPortalName.equals(oldPortalName)) {
					Vector3[] portalCorners = Main.portalOperator.getPortalCorners(oldPortalName);
					
					player.showFormWindow(new PortalReplace(newPortalName, possibleNewIPAddress, possibleNewPort.toString(), portalCorners[0].toString(), portalCorners[1].toString(), oldPortalName));
					return;
				}
				
				if (!newPortalName.equals(oldPortalName)) {
					Main.portalOperator.deletePortal(oldPortalName);
				}
				
				Main.portalOperator.createPortal(possibleNewIPAddress, possibleNewPort, Main.portalOperator.getPortalCorners(oldPortalName), newPortalName);
				
				player.sendMessage("Successfully edited portal.");
			}
		}
	}
}
