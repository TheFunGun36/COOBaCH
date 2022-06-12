package jora.coobach;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.world.World;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class ThermalGenerator extends Block {

    public static int getLightLevel(BlockState state) {
        return state.get(Properties.LIT) ? 13 : 0;
    }

    public ThermalGenerator(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.LIT, false).with(Properties.FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.LIT);
		builder.add(Properties.FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.setBlockState(pos, state.with(Properties.LIT, !state.get(Properties.LIT)));
        
        return ActionResult.SUCCESS;
    }

    @Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return (BlockState)this.getDefaultState().with(Properties.FACING, ctx.getPlayerFacing().getOpposite());
	}

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(Properties.LIT).booleanValue()) {
            return;
        }
        double cx = (double)pos.getX() + 0.5;
        double y = pos.getY();
        double cz = (double)pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            world.playSound(cx, y, cz, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }
        Direction direction = state.get(Properties.FACING);
        Direction.Axis axis = direction.getAxis();
        double h = random.nextDouble() * 0.6 - 0.3;
        double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52 : h;
        double j = random.nextDouble() * 6.0 / 16.0;
        double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52 : h;
        world.addParticle(ParticleTypes.SMOKE, cx + i, y + j, cz + k, 0.0, 0.0, 0.0);
        world.addParticle(ParticleTypes.FLAME, cx + i, y + j, cz + k, 0.0, 0.0, 0.0);
    }
}
