package smol.retroitems;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
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
