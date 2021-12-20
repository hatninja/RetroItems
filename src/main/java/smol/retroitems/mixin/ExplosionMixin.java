package smol.retroitems.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smol.retroitems.RetroItems;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
	//Prevent explosion drops from merging.
	@Inject(method = "tryMergeStack", at = @At("HEAD"), cancellable = true)
	private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, ItemStack itemStack, BlockPos blockPos, CallbackInfo ci) {
		if (!RetroItems.CONFIG.explosionDrops) { return; }

		objectArrayList.add(Pair.of(itemStack, blockPos));
		ci.cancel();
	}
}
