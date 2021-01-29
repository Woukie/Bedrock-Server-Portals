package main.java.org.untreat.serverportals.forms;

import java.util.Arrays;

import main.java.org.untreat.serverportals.Main;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;

public class PortalEditForm extends FormWindowCustom {
	public PortalEditForm(String portalName) {
		super("Server Portals: Edit Portal " + portalName, Arrays.asList(new Element[] {
				new ElementInput("Name", "", portalName),
				new ElementInput("IP", "", Main.portalOperator.getPortalAddress(portalName)[0]),
				new ElementInput("Port", "", Main.portalOperator.getPortalAddress(portalName)[1]),
		}));
	}
}
