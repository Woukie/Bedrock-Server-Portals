package main.java.org.untreat.serverportals;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cn.nukkit.math.Vector3;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class PortalObject {
	public String IPAddress;
	public Integer Port;
	public String portalName;
	
	private Vector3[] corners;
	
	public PortalObject(String IPAddress, Integer Port, Vector3[] corners, String portalName) {
		this.portalName = portalName;
		this.IPAddress = IPAddress;
		this.Port = Port;
		
		Vector3[] newCorners = new Vector3[2];
		newCorners[0] = new Vector3(corners[0].x < corners[1].x ? corners[0].x : corners[1].x, corners[0].y < corners[1].y ? corners[0].y : corners[1].y, corners[0].z < corners[1].z ? corners[0].z : corners[1].z);
		newCorners[1] = new Vector3(corners[0].x > corners[1].x ? corners[0].x : corners[1].x, corners[0].y > corners[1].y ? corners[0].y : corners[1].y, corners[0].z > corners[1].z ? corners[0].z : corners[1].z);
		this.corners = newCorners;
	}
		
	public Vector3[] getCorners() {
		return corners;
	}
	
	// Deletes portals on the json with the same name
	public void savePortal() {
		JSONArray portalData = new JSONArray();
		JSONObject portalDataToAdd = new JSONObject();
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		
		try {
			FileReader reader = new FileReader(Main.server.getPluginPath() + "/portals.json");
			portalData = (JSONArray)parser.parse(reader);
			reader.close();
		} catch (Exception ignore) { }
		
		portalData.removeIf(portalJSONObject -> (
			portalName.equals(((JSONObject)portalJSONObject).get("Portal Name").toString())
		));
		
		portalDataToAdd.put("IPAddress", IPAddress);
		portalDataToAdd.put("Port", Port.toString());
		portalDataToAdd.put("Portal Name", portalName);
		portalDataToAdd.put("X0", corners[0].x);
		portalDataToAdd.put("Y0", corners[0].y);
		portalDataToAdd.put("Z0", corners[0].z);
		portalDataToAdd.put("X1", corners[1].x);
		portalDataToAdd.put("Y1", corners[1].y);
		portalDataToAdd.put("Z1", corners[1].z);
		
		portalData.add(portalDataToAdd);
		
		try {
			FileWriter writer = new FileWriter(Main.server.getPluginPath() + "/portals.json");
			writer.write(portalData.toJSONString());
			writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}
