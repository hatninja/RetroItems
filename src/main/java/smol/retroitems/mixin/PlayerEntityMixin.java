package smol.retroitems.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import smol.retroitems.RetroItems;

import java.util.Random;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
	private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
		if (!RetroItems.CONFIG.playerDrops) {
			return;
		}
		if (this.world.isClient) {
			this.swingHand(Hand.MAIN_HAND);
		}
		if (!this.world.isClient && !stack.isEmpty()) {
			Random random = this.random;

			int stackCount = stack.getCount();
			stack.setCount(1);
			for (int i = 0; i < stackCount; i++) {
				ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getEyeY() - 0.3D, this.getZ(), stack);
				itemEntity.setPickupDelay(40);
				if (retainOwnership) {
					itemEntity.setThrower(this.getUuid());
				}

				float spreadYaw = random.nextFloat() * 6.2831855F;
				float spreadDist = random.nextFloat();
				if (throwRandomly) {
					spreadDist *= 0.5F;
					itemEntity.setVelocity(-Math.sin(spreadYaw) * spreadDist, 0.2D, Math.cos(spreadYaw) * spreadDist);
				} else {
					spreadDist *= 0.04F; //It's +0.02 for funsies~

					float sinPitch = MathHelper.sin(this.getPitch() * 0.017453292F);
					float cosPitch = MathHelper.cos(this.getPitch() * 0.017453292F);
					float sinYaw = MathHelper.sin(this.getYaw() * 0.017453292F);
					float cosYaw = MathHelper.cos(this.getYaw() * 0.017453292F);
					float distance = 0.3F;

					itemEntity.setVelocity((-sinYaw*cosPitch * distance) + Math.sin(spreadYaw) * spreadDist, (-sinPitch * distance + 0.1F + (random.nextFloat() - random.nextFloat()) * 0.1F), (cosYaw*cosPitch * distance) + Math.cos(spreadYaw) * spreadDist);
				}

				this.world.spawnEntity(itemEntity);
			}
		}
		cir.cancel();
	}
}
