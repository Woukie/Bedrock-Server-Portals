package main.java.org.untreat.serverportals;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class PortalOperator {
	private ArrayList<PortalObject> portals = new ArrayList<PortalObject>();
	private ArrayList<Player> showHighlightPlayers = new ArrayList<Player>();
	private HashMap<Player, Vector3f> previousPlayerPositions = new HashMap<Player, Vector3f>();
	private HashMap<Player, Vector3[]> playerSelections = new HashMap<Player, Vector3[]>();
	
	// Create a bunch of portals without saving them
	public void loadPortals() {
		JSONArray portalData = new JSONArray();
		try {
			JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			FileReader reader = new FileReader(Main.server.getPluginPath() + "/portals.json");
			portalData = (JSONArray)parser.parse(reader);
			reader.close();
		} catch (Exception ignore) {
			Main.server.getLogger().info("Error loading portals!");
		}
		
		portalData.forEach(portal -> {
			String IPAddress = ((JSONObject)portal).get("IPAddress").toString();
			String portalName = ((JSONObject)portal).get("Portal Name").toString();
			Integer port = Integer.parseInt(((JSONObject)portal).get("Port").toString());
			Double X0 = Double.parseDouble(((JSONObject)portal).get("X0").toString());
			Double Y0 = Double.parseDouble(((JSONObject)portal).get("Y0").toString());
			Double Z0 = Double.parseDouble(((JSONObject)portal).get("Z0").toString());
			Double X1 = Double.parseDouble(((JSONObject)portal).get("X1").toString());
			Double Y1 = Double.parseDouble(((JSONObject)portal).get("Y1").toString());
			Double Z1 = Double.parseDouble(((JSONObject)portal).get("Z1").toString());
			
			Vector3[] cornersV = {new Vector3(X0, Y0, Z0), new Vector3(X1, Y1, Z1)};
			
			portals.add(new PortalObject(IPAddress, port, cornersV, portalName));
		});
	}
	
	// Also deletes all portals with that name from the portals arraylist as constructing a new portal will delete any with the same name in the json
	public void createPortal(String IPAddress, Integer Port, Vector3[] corners, String portalName) {
		portals.removeIf(portal -> (
			portalName.equals(portal.portalName)
		));
		
		PortalObject portal = new PortalObject(IPAddress, Port, corners, portalName);
		portal.savePortal();
		portals.add(portal);
	}
	
	public void deletePortal(String name) {
		JSONArray portalData = new JSONArray();
		try {
			JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			FileReader reader = new FileReader(Main.server.getPluginPath() + "/portals.json");
			portalData = (JSONArray)parser.parse(reader);
			reader.close();
		} catch (Exception ignore) {
			Main.server.getLogger().info("Error loading portals!");
		}
		
		portalData.removeIf(portal -> ((JSONObject)portal).get("Portal Name").equals(name));
		portals.removeIf(portal -> (portal.portalName.equals(name)));
		
		try {
			FileWriter writer = new FileWriter(Main.server.getPluginPath() + "/portals.json");
			writer.write(portalData.toJSONString());
			writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	// Return IP address and port as a string array.
	public String[] getPortalAddress(String portalName) {
		for (PortalObject portal : portals) {
			if (portal.portalName.equals(portalName)) {
				return new String[] {portal.IPAddress,"" + portal.Port};
			}
		}
		Main.server.getLogger().info("Couldn't find portal address");
		return null;
	}
	
	public void setPortalAddress(String portalName, String IPAddress, Integer port) {
		portals.forEach(portal -> {
			if  (portal.portalName.equals(portalName)) {
				portal.IPAddress = IPAddress;
				portal.Port = port;
				portal.savePortal();
				return;
			}
		});
	}
	
	public List<String> getPortalNames() {
		ArrayList<String> portalNameList = new ArrayList<String>();
		portals.forEach(portal -> {
			portalNameList.add(portal.portalName);
		});
		
		return portalNameList;
	}
	
	// Replaces it if it allready exists
	public void setPortalName(String portalName, String newPortalName) {
		if (doesPortalExist(newPortalName)) {
			deletePortal(newPortalName);
		}
		
		portals.forEach(portal -> {
			if  (portal.portalName.equals(portalName)) {
				portal.portalName = newPortalName;
				portal.savePortal();
				deletePortal(portalName);
				return;
			}
		});
	}

	public Vector3[] getPortalCorners(String portalName) {
		Vector3 corner0 = new Vector3();
		Vector3 corner1 = new Vector3();
		
		portals.forEach(portal -> {
			if (portal.portalName.equals(portalName) ) {
				Vector3[] corners = portal.getCorners();
				corner0.setComponents(corners[0]);
				corner1.setComponents(corners[1]);
				
				return;
			};
		});
		
		return new Vector3[] {corner0, corner1};
	}
	
	public boolean doesPortalExist(String name) {
		for (PortalObject portal : portals) {
			if (portal.portalName.equals(name)) return true;
		}
		return false;
	}
	
	public boolean addLeftSelection(Vector3 position, Player player) {
		try {
			if (!playerSelections.containsKey(player)) {
				playerSelections.put(player, new Vector3[] {position, null});
			} else {
				playerSelections.put(player, new Vector3[] {position, playerSelections.get(player)[1]});
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean addRightSelection(Vector3 position, Player player) {
		try {
			if (!playerSelections.containsKey(player)) {
				playerSelections.put(player, new Vector3[] {null, position});
			} else {
				playerSelections.put(player, new Vector3[] {playerSelections.get(player)[0], position});
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Vector3[] getPlayerSelection(Player player) {
		if (playerSelections.get(player)[0] != null && playerSelections.get(player)[1] != null) {
			return playerSelections.get(player);
		}
		return null;
	}
	
	public boolean showPortalsToPlayer(Player player) {
		if (showHighlightPlayers.contains(player)) {
			return false;
		}
		showHighlightPlayers.add(player);
		return true;
	}
	
	public boolean hidePortalsFromPlayer(Player player) {
		if (showHighlightPlayers.contains(player)) {
			showHighlightPlayers.remove(player);
			return true;
		}
		
		return false;
	}
	
	public void checkAndSendPlayer(Player player) {
		Vector3f playerLocation = player.getLocation().asVector3f();
		
		if (!previousPlayerPositions.containsKey(player)) {
			previousPlayerPositions.put(player, playerLocation);
			return;
		}
		
		portals.forEach(portal -> {
			
			Boolean playerNowInPortal = positionInPortal(portal, playerLocation);
			Boolean playerWasInPortal = positionInPortal(portal, previousPlayerPositions.get(player));
			
			if (playerNowInPortal && !playerWasInPortal) {
				InetSocketAddress portalServerAddress = new InetSocketAddress(portal.IPAddress, portal.Port);
				
				player.transfer(portalServerAddress);
			}
		});
		previousPlayerPositions.put(player, playerLocation);
	}
	
	private Boolean positionInPortal(PortalObject portal, Vector3f playerLocation) {
		Vector3[] corners = portal.getCorners();
		Boolean alignsX = (playerLocation.x < corners[1].x + 1 && playerLocation.x > corners[0].x) ? true : false;
		Boolean alignsY = (playerLocation.y < corners[1].y + 1 && playerLocation.y >= corners[0].y - 1) ? true : false;
		Boolean alignsZ = (playerLocation.z < corners[1].z + 1 && playerLocation.z > corners[0].z) ? true : false;
		
		return (alignsX && alignsY && alignsZ);
	}

	public void showParticles() {
		ArrayList<Particle> particlesToSpawn = new ArrayList<Particle>();
		
		portals.forEach(portal -> {
			Vector3[] corners = portal.getCorners();
			
			for (int x = (int) corners[0].x; x <= corners[1].x + 1; x++) {
				for (int z = (int) corners[0].z; z <= corners[1].z; z++) {
					particlesToSpawn.add(new CriticalParticle(new Vector3(x, corners[0].y, z)));
					particlesToSpawn.add(new CriticalParticle(new Vector3(x, corners[1].y + 1, z)));
				}
			}
			
			for (int y = (int) corners[0].y; y <= corners[1].y + 1; y++) {
				for (int x = (int) corners[0].x; x <= corners[1].x; x++) {
					particlesToSpawn.add(new CriticalParticle(new Vector3(x, y, corners[0].z)));
					particlesToSpawn.add(new CriticalParticle(new Vector3(x, y, corners[1].z + 1)));
				}
				for (int z = (int) corners[0].z; z <= corners[1].z + 1; z++) {
					particlesToSpawn.add(new CriticalParticle(new Vector3(corners[0].x, y, z)));
					particlesToSpawn.add(new CriticalParticle(new Vector3(corners[1].x + 1, y, z)));
				}
			}
			
			particlesToSpawn.forEach(particle -> {
				Main.server.getDefaultLevel().addParticle(particle, showHighlightPlayers);				
			});
		});
	}
}
