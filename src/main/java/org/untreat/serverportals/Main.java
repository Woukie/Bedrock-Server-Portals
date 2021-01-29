package main.java.org.untreat.serverportals;

import main.java.org.untreat.serverportals.forms.FormHandler;
import main.java.org.untreat.serverportals.input.PortalEvents;
import main.java.org.untreat.serverportals.input.PortalsCommand;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase {
	public static Server server;
	public static PortalOperator portalOperator;
	
	@Override
	public void onEnable() {
		server = this.getServer();
		portalOperator = new PortalOperator();
		
		server.getCommandMap().register("portal", new PortalsCommand());
		
		server.getPluginManager().registerEvents(new FormHandler(), this);
		server.getPluginManager().registerEvents(new PortalEvents(), this);
		
		server.getScheduler().scheduleRepeatingTask(new ShowParticlesTimer(this), 5);
		
		portalOperator.loadPortals();
	}
}
