package main.java.org.untreat.serverportals;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;

public class ShowParticlesTimer extends PluginTask<Plugin> {
	
	public ShowParticlesTimer(Plugin plugin) {
		super(plugin);
	}
	
	@Override
	public void onRun(int currentTick) {
		Main.portalOperator.showParticles();
	}
}
