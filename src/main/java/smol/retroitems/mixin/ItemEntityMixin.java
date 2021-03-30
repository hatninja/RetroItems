package smol.retroitems.mixin;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import smol.retroitems.RetroItems;

//https://fabricmc.net/wiki/tutorial:mixin_injects

//This class is the first thing i did in fabric modding. I don't care and i love it.

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Inject(method = "canMerge()Z", at = @At("HEAD"), cancellable = true)
	private void canMerge(CallbackInfoReturnable<Boolean> info) {
		if (RetroItems.CONFIG.merge) { return; }
		info.setReturnValue(false);
	}

	//For now, item splitting is best handled case-by-case.
	/*@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	public void ItemEntity(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
		int stackCount = stack.getCount();
		stack.setCount(1);
		for (int i=1;i<stackCount;i++) {
			ItemEntity newItem = new ItemEntity(world,x,y,z,stack);
			world.spawnEntity(newItem);
		}
	}*/
}
