package smol.retroitems.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smol.retroitems.RetroItems;

import java.util.Random;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
    @Shadow private ItemRenderer itemRenderer;
    @Shadow private Random random;

    private static final Quaternion flipQuat = Vector3f.POSITIVE_Y.getRadialQuaternion(3.14159F);

    protected ItemEntityRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(ItemEntity itemEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        matrixStack.push();
        ItemStack itemStack = itemEntity.getStack();

        //Shadows
        this.shadowRadius = RetroItems.CONFIG.shadows ? 0.15F : 0.0F;

        BakedModel bakedModel = this.itemRenderer.getHeldItemModel(itemStack, itemEntity.world, null);
        boolean hasDepth = bakedModel.hasDepth() && !RetroItems.CONFIG.blocksFlat;

        //Hovering
        float hoverOffset = MathHelper.sin((itemEntity.getAge() + tickDelta) / 10.0F + itemEntity.hoverHeight) * 0.1F + 0.1F;
        float hoverScale = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
        matrixStack.translate(0.0D, (hoverOffset + 0.25F * hoverScale), 0.0D);

        //Rotation
        boolean doRotate = hasDepth || RetroItems.CONFIG.rotatingItems;
        if (doRotate) {
            float rotateBy = (itemEntity.getAge() + tickDelta) / 20.0F;
            matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotateBy));
        } else {
            //Face the camera
            matrixStack.multiply(this.dispatcher.getRotation());
            if (!RetroItems.CONFIG.classicDraw) {
                matrixStack.multiply(flipQuat);
            }
        }

        //Drawing
        if (hasDepth || !RetroItems.CONFIG.classicDraw) {
            this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV, bakedModel);
        } else {
            Sprite sprite = bakedModel.getSprite();

            float minU = sprite.getMinU();
            float minV = sprite.getMinV();
            float maxU = sprite.getMaxU();
            float maxV = sprite.getMaxV();

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntityCutout());
            MatrixStack.Entry entry = matrixStack.peek();
            drawItemVertex(entry, vertexConsumer, 0.25F, -0.125F, 0F, minU, maxV, light);
            drawItemVertex(entry, vertexConsumer, -0.25F, -0.125F, 0F, maxU, maxV, light);
            drawItemVertex(entry, vertexConsumer, -0.25F, 0.375F, 0F, maxU, minV, light);
            drawItemVertex(entry, vertexConsumer, 0.25F, 0.375F, 0F, minU, minV, light);

            if (doRotate) { //There's no need to draw the backside if it always faces the camera.
                drawItemVertex(entry, vertexConsumer, -0.25F, -0.125F, 0F, maxU, maxV, light);
                drawItemVertex(entry, vertexConsumer, 0.25F, -0.125F, 0F, minU, maxV, light);
                drawItemVertex(entry, vertexConsumer, 0.25F, 0.375F, 0F, minU, minV, light);
                drawItemVertex(entry, vertexConsumer, -0.25F, 0.375F, 0F, maxU, minV, light);
            }
        }

        matrixStack.pop();
        super.render(itemEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);

        ci.cancel();
    }

    private static void drawItemVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v, int light) {
        vertices.vertex(entry.getModel(),x,y,z)
                .color(255,255,255,255)
                .texture(u,v)
                .overlay(0,10)
                .light(light)
                .normal(entry.getNormal(),0F,1.0F,0.0F)
                .next();
    }
}
