package re.domi.invisiblights;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

@SuppressWarnings("WeakerAccess")
public class Config
{
    public static int PoweredLightRodCapacity;
    public static int PoweredLightRodCost;

    public static void load()
    {
        ForgeConfig conf = new ForgeConfig();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, conf.configSpec);
        conf.load(FMLPaths.CONFIGDIR.get().resolve("invisiblights-common.toml"));

        PoweredLightRodCapacity = conf.poweredLightRodCapacity.get();
        PoweredLightRodCost = conf.poweredLightRodCost.get();
    }

    private static class ForgeConfig
    {
        private ForgeConfigSpec configSpec;

        private ConfigValue<Integer> poweredLightRodCapacity;
        private ConfigValue<Integer> poweredLightRodCost;

        private ForgeConfig()
        {
            Builder builder = new Builder();

            builder.push("general");

            this.poweredLightRodCapacity = builder.comment("How much Energy the powered Rod can hold.").define("PoweredLightRodCapacity", 2560000);
            this.poweredLightRodCost = builder.comment("The Energy required to place a light source.").define("PoweredLightRodCost", 10000);

            builder.pop();

            this.configSpec = builder.build();
        }

        private void load(Path path)
        {
            CommentedFileConfig config = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

            config.load();
            this.configSpec.setConfig(config);
        }
    }
}
