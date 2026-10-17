package crystal.champions.client.mixin;

import crystal.champions.client.raycast.ChampionsRayCast;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(RaycastContext.ShapeType.class)
public abstract class ShapeTypeMixin {

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private static void setExceptionForChampions(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext, CallbackInfoReturnable<VoxelShape> cir) {
        final Set<Block> seeThrough = ChampionsRayCast.active();
        if (seeThrough != null && seeThrough.contains(blockState.getBlock())) {
            cir.setReturnValue(VoxelShapes.empty());
        }
    }
}