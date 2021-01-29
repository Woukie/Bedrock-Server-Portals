package main.java.org.untreat.serverportals.forms;

import java.util.Arrays;

import main.java.org.untreat.serverportals.Main;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.window.FormWindowCustom;

public class PortalSelectEditForm extends FormWindowCustom {
	public PortalSelectEditForm() {
		super("Server Portals: Edit Portal", Arrays.asList(new Element[] {
				new ElementDropdown("Portal Name", Main.portalOperator.getPortalNames())
		}));
	}
}