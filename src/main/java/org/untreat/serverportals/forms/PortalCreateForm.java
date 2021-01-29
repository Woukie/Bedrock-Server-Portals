package main.java.org.untreat.serverportals.forms;

import java.util.Arrays;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;

public class PortalCreateForm extends FormWindowCustom {
	public PortalCreateForm() {
		super("Server Portals: Create Portal", Arrays.asList(new Element[] {
				new ElementInput("Portal Name"),
				new ElementInput("Server IP"),
				new ElementInput("Server Port"),
		}));
	}
}