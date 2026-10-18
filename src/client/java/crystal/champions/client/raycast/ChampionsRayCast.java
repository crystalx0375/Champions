package crystal.champions.client.raycast;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Set;
import java.util.function.Supplier;

public class ChampionsRayCast {
    private static final ThreadLocal<Set<Block>> ACTIVE = new ThreadLocal<>();

    protected static final Set<Block> SET_SEE_THROUGH = Set.of(
            Blocks.GLASS,
            Blocks.WHITE_STAINED_GLASS,
            Blocks.ORANGE_STAINED_GLASS,
            Blocks.MAGENTA_STAINED_GLASS,
            Blocks.LIGHT_BLUE_STAINED_GLASS,
            Blocks.YELLOW_STAINED_GLASS,
            Blocks.LIME_STAINED_GLASS,
            Blocks.PINK_STAINED_GLASS,
            Blocks.GRAY_STAINED_GLASS,
            Blocks.LIGHT_GRAY_STAINED_GLASS,
            Blocks.CYAN_STAINED_GLASS,
            Blocks.PURPLE_STAINED_GLASS,
            Blocks.BLUE_STAINED_GLASS,
            Blocks.BROWN_STAINED_GLASS,
            Blocks.GREEN_STAINED_GLASS,
            Blocks.RED_STAINED_GLASS,
            Blocks.BLACK_STAINED_GLASS,
            Blocks.GLASS_PANE,
            Blocks.WHITE_STAINED_GLASS_PANE,
            Blocks.ORANGE_STAINED_GLASS_PANE,
            Blocks.MAGENTA_STAINED_GLASS_PANE,
            Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
            Blocks.YELLOW_STAINED_GLASS_PANE,
            Blocks.LIME_STAINED_GLASS_PANE,
            Blocks.PINK_STAINED_GLASS_PANE,
            Blocks.GRAY_STAINED_GLASS_PANE,
            Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
            Blocks.CYAN_STAINED_GLASS_PANE,
            Blocks.PURPLE_STAINED_GLASS_PANE,
            Blocks.BLUE_STAINED_GLASS_PANE,
            Blocks.BROWN_STAINED_GLASS_PANE,
            Blocks.GREEN_STAINED_GLASS_PANE,
            Blocks.RED_STAINED_GLASS_PANE,
            Blocks.BLACK_STAINED_GLASS_PANE,
            Blocks.TINTED_GLASS,
            Blocks.OAK_LEAVES,
            Blocks.SPRUCE_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.CHERRY_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.MANGROVE_LEAVES,
            Blocks.AZALEA_LEAVES,
            Blocks.FLOWERING_AZALEA_LEAVES,
            Blocks.OAK_FENCE,
            Blocks.SPRUCE_FENCE,
            Blocks.BIRCH_FENCE,
            Blocks.JUNGLE_FENCE,
            Blocks.ACACIA_FENCE,
            Blocks.CHERRY_FENCE,
            Blocks.DARK_OAK_FENCE,
            Blocks.MANGROVE_FENCE,
            Blocks.BAMBOO_FENCE,
            Blocks.CRIMSON_FENCE,
            Blocks.WARPED_FENCE,
            Blocks.OAK_FENCE_GATE,
            Blocks.SPRUCE_FENCE_GATE,
            Blocks.BIRCH_FENCE_GATE,
            Blocks.JUNGLE_FENCE_GATE,
            Blocks.ACACIA_FENCE_GATE,
            Blocks.CHERRY_FENCE_GATE,
            Blocks.DARK_OAK_FENCE_GATE,
            Blocks.MANGROVE_FENCE_GATE,
            Blocks.BAMBOO_FENCE_GATE,
            Blocks.CRIMSON_FENCE_GATE,
            Blocks.WARPED_FENCE_GATE
    );

    protected static <T> T SET_FLAG(Supplier<T> fn) {
        ACTIVE.set(ChampionsRayCast.SET_SEE_THROUGH);
        try {
            return fn.get();
        } finally {
            ACTIVE.remove();
        }
    }

    public static Set<Block> active() {
        return ACTIVE.get();
    }
}