package jora.coobach.block;

import jora.coobach.COOBaCH;
import jora.coobach.block.entity.ThermalGeneratorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.world.World;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class ThermalGeneratorBlock extends BlockWithEntity {

    public static int getLightLevel(BlockState state) {
        return state.get(Properties.LIT) ? 13 : 0;
    }

    public ThermalGeneratorBlock(Settings settings) {
        super(settings
            .luminance(ThermalGeneratorBlock::getLightLevel)
            .strength(5, 8)
            .sounds(BlockSoundGroup.METAL)
            .requiresTool());
        setDefaultState(this.stateManager.getDefaultState().with(Properties.LIT, false).with(Properties.FACING, Direction.NORTH));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, COOBaCH.THERMAL_GENERATOR_BLOCK_ENTITY, (world1, pos, state1, be) -> ThermalGeneratorBlockEntity.tick(world1, pos, state1, be));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThermalGeneratorBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.LIT);
		builder.add(Properties.FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null)
                player.openHandledScreen(screenHandlerFactory);
        }        
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ThermalGeneratorBlockEntity) {
                ItemScatterer.spawn(world, pos, (ThermalGeneratorBlockEntity)blockEntity);
                world.removeBlockEntity(pos);
                world.updateComparators(pos, this);
            }
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
 
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
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
