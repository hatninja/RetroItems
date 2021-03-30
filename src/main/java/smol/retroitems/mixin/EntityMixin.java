package smol.retroitems.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import smol.retroitems.RetroItems;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow public World world;
	@Shadow public abstract double getX();
	@Shadow public abstract double getY();
	@Shadow public abstract double getZ();

	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
	private void dropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
		if (!RetroItems.CONFIG.mobDrops) { return; }
		if (!this.world.isClient && !stack.isEmpty()) {
			int stackCount = stack.getCount();
			stack.setCount(1);
			for (int i=0;i<stackCount;i++) {
				world.spawnEntity(new ItemEntity(this.world, this.getX(), this.getY() + yOffset, this.getZ(), stack));
			}
		}
		cir.setReturnValue(null);
	}
}
