package smol.retroitems;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import smol.retroitems.config.RetroItemsConfig;

public class RetroItems implements ModInitializer {
	public static final String MOD_ID = "retroitems";
	public static RetroItemsConfig CONFIG;

	@Override
	public void onInitialize() {
		AutoConfig.register(RetroItemsConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(RetroItemsConfig.class).getConfig();
	}
}
