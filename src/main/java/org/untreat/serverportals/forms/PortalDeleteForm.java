package main.java.org.untreat.serverportals.forms;

import java.util.Arrays;

import main.java.org.untreat.serverportals.Main;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;

public class PortalDeleteForm extends FormWindowCustom {
	public PortalDeleteForm() {
		super("Server Portals: Delete Portal", Arrays.asList(new Element[] {
				new ElementLabel("Delete portal: "),
				new ElementDropdown("Portal Name", Main.portalOperator.getPortalNames())
		}));
	}
}
