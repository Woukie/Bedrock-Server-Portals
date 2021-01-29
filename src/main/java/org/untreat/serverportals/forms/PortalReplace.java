package main.java.org.untreat.serverportals.forms;

import java.util.Arrays;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;

public class PortalReplace extends FormWindowCustom{
	public PortalReplace(String toPortalName, String IPAddress, String Port, String Corner0, String Corner1, String fromPortalName) {
		super("Server Portals: Replace Portal " + toPortalName + "?", Arrays.asList(new Element[] {
				new ElementLabel(fromPortalName + "\n" + IPAddress + "\n" + Port + "\n" + Corner0.replace("Vector3", "") + "\n" + Corner1.replace("Vector3", ""))
		}));
	}
}
