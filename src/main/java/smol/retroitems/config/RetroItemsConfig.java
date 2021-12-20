package smol.retroitems.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import smol.retroitems.RetroItems;

@Config(name = RetroItems.MOD_ID)
public class RetroItemsConfig implements ConfigData {
    @Comment("Enable fast item rendering. (Some items will not render properly.)")
    public boolean classicDraw = false;
    @Comment("Whether shadows will draw under items.")
    public boolean shadows = false;
    @Comment("Generate item models as flat. (Reload using F3+T)")
    public boolean allFlat = false;
    @Comment("Make items rotate instead of facing the camera.")
    public boolean rotatingItems = false;
    @Comment("Render blocks just like items. (Fast rendering required)")
    public boolean blocksFlat = false;
    @Comment("Allow dropped items to merge.")
    public boolean merge = false;
    @Comment("Enable blocks to drop items individually.")
    public boolean blockDrops = true;
    @Comment("Enable mobs to drop items individually.")
    public boolean mobDrops = true;
    @Comment("Enable players to drop items individually.")
    public boolean playerDrops = true;
    @Comment("Enable explosions to drop items individually.")
    public boolean explosionDrops = true;
}