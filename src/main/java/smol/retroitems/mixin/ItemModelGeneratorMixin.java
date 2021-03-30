package smol.retroitems.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import smol.retroitems.RetroItems;

import java.util.List;
import java.util.Map;

@Mixin(ItemModelGenerator.class)
public abstract class ItemModelGeneratorMixin  {

    @Inject(method="addLayerElements", at=@At("HEAD"), cancellable = true)
    private void addLayerElements(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
        //Enable flat models
        if (!RetroItems.CONFIG.allFlat) { return; }

        Map<Direction, ModelElementFace> map = Maps.newHashMap();
        map.put(Direction.SOUTH, new ModelElementFace(null, layer, key, new ModelElementTexture(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0)));
        map.put(Direction.NORTH, new ModelElementFace(null, layer, key, new ModelElementTexture(new float[]{16.0F, 0.0F, 0.0F, 16.0F}, 0)));
        List<ModelElement> list = Lists.newArrayList();
        list.add(new ModelElement(new Vector3f(0.0F, 0.0F, 8.0F), new Vector3f(16.0F, 16.0F, 8.0F), map, null, false));
        cir.setReturnValue(list);
    }
}
