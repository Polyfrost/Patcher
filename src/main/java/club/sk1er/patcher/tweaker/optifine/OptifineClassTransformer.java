package club.sk1er.patcher.tweaker.optifine;

import club.sk1er.patcher.tweaker.ClassTransformer;
import club.sk1er.patcher.tweaker.asm.optifine.EntityRendererTransformer;
import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptifineClassTransformer implements IClassTransformer {

  private final Logger LOGGER = LogManager.getLogger("OptifinePatcherTransformer");
  private final Multimap<String, PatcherTransformer> transformerMap = ArrayListMultimap.create();
  private final boolean outputBytecode =
      Boolean.parseBoolean(System.getProperty("debugBytecode", "false"));

  public OptifineClassTransformer() {
    registerTransformer(new EntityRendererTransformer());
  }

  private void registerTransformer(PatcherTransformer transformer) {
    for (String cls : transformer.getClassName()) {
      transformerMap.put(cls, transformer);
    }
  }

  @Override
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    return ClassTransformer.createTransformer(
        transformedName, bytes, transformerMap, LOGGER, outputBytecode);
  }
}
