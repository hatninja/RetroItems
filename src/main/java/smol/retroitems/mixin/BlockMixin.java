package smol.retroitems.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smol.retroitems.RetroItems;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(method = "dropStack", at = @At("HEAD"), cancellable = true)
	private static void dropStack(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
		if (!RetroItems.CONFIG.blockDrops) { return; }
		if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			int stackCount = stack.getCount();
			stack.setCount(1);
			for (int i=0;i<stackCount;i++) {
				double x = pos.getX() + (world.random.nextFloat() * 0.5F) + 0.25D;
				double y = pos.getY() + (world.random.nextFloat() * 0.5F) + 0.25D;
				double z = pos.getZ() + (world.random.nextFloat() * 0.5F) + 0.25D;
				world.spawnEntity(new ItemEntity(world, x,y,z, stack));
			}
		}
		ci.cancel();
	}
}
