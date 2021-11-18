package codes.sana.cloudnine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;

public class CloudNineConfig {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    
    private static ForgeConfigSpec.ConfigValue<Integer> CloudOffset;

    public static ForgeConfigSpec getConfigSpec() {
        CloudOffset = builder
                        .comment("Set the offset from the clouds default height of 128")
                        .translation("cloudnine.config.cloudOffset.title")
                        .defineInRange("cloudnine.config.cloudOffset.value", 0, -100, 100);
        return builder.build();
    }
}
