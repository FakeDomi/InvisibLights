package re.domi.invisiblights;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

import java.nio.file.Path;

public class Config
{
    public static int PoweredLightRodCapacity;
    public static int PoweredLightRodCost;

    public static class Forge
    {
        public static ForgeConfigSpec ConfigSpec;

        private static ConfigValue<Integer> PoweredLightRodCapacity;
        private static ConfigValue<Integer> PoweredLightRodCost;

        static
        {
            Builder builder = new Builder();

            builder.push("general");

            PoweredLightRodCapacity = builder.comment("How much Energy the powered Rod can hold.").define("PoweredLightRodCapacity", 2560000);
            PoweredLightRodCost = builder.comment("The Energy required to place a light source.").define("PoweredLightRodCost", 10000);

            builder.pop();

            ConfigSpec = builder.build();
        }

        public static void load(ForgeConfigSpec spec, Path path)
        {
            CommentedFileConfig config = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

            config.load();
            spec.setConfig(config);

            Config.PoweredLightRodCapacity = PoweredLightRodCapacity.get();
            Config.PoweredLightRodCost = PoweredLightRodCost.get();
        }
    }
}
