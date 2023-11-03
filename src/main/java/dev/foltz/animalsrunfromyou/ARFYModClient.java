package dev.foltz.animalsrunfromyou;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class ARFYModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ARFYModConfig.class, JanksonConfigSerializer::new);
    }
}
