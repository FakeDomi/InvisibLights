package re.domi.invisiblights.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{
    public static boolean PlaceVanillaLightSourceBlock = false;
    public static int LightSourceGlowstoneCost = 2;
    public static long PoweredLightRodCapacity = 640000;
    public static long PoweredLightRodChargeRate = 6400;
    public static long PoweredLightSourceCost = 2000;

    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "invisiblights.properties");
    private static final String CONFIG_COMMENT = "InvisibLights config file";

    public static void read()
    {
        try
        {
            if (CONFIG_FILE.createNewFile())
            {
                write();
                return;
            }

            Properties p = new Properties();
            p.load(new FileInputStream(CONFIG_FILE));

            PlaceVanillaLightSourceBlock = Boolean.toString(true).equals(p.getProperty("PlaceVanillaLightSourceBlock"));
            LightSourceGlowstoneCost = Integer.parseInt(p.getProperty("LightSourceGlowstoneCost"));

            PoweredLightRodCapacity = Long.parseLong(p.getProperty("PoweredLightRodCapacity"));
            PoweredLightRodChargeRate = Long.parseLong(p.getProperty("PoweredLightRodChargeRate"));
            PoweredLightSourceCost = Long.parseLong(p.getProperty("PoweredLightSourceCost"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void write()
    {
        try
        {
            Properties p = new Properties();

            p.setProperty("PlaceVanillaLightSourceBlock", Boolean.toString(PlaceVanillaLightSourceBlock));
            p.setProperty("LightSourceGlowstoneCost", Integer.toString(LightSourceGlowstoneCost));

            p.setProperty("PoweredLightRodCapacity", Long.toString(PoweredLightRodCapacity));
            p.setProperty("PoweredLightRodChargeRate", Long.toString(PoweredLightRodChargeRate));
            p.setProperty("PoweredLightSourceCost", Long.toString(PoweredLightSourceCost));

            p.store(new FileOutputStream(CONFIG_FILE), CONFIG_COMMENT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
