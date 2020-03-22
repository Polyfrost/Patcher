package club.sk1er.patcher.hooks;

import com.google.common.collect.ImmutableSetMultimap;
import java.util.Map;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ForgeChunkManagerHook {

  public static ImmutableSetMultimap<ChunkCoordIntPair, Ticket> getPersistentChunksFor(
      World world, Map<World, ImmutableSetMultimap<ChunkCoordIntPair, Ticket>> forcedChunks) {
    if (world.isRemote) return ImmutableSetMultimap.of();
    ImmutableSetMultimap<ChunkCoordIntPair, Ticket> persistentChunks = forcedChunks.get(world);
    return persistentChunks != null ? persistentChunks : ImmutableSetMultimap.of();
  }
}