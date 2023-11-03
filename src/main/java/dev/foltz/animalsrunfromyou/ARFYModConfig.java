package dev.foltz.animalsrunfromyou;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "animals-run-from-you")
public class ARFYModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Stats chicken = new Stats(1.2f, 2f, 32f);

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Stats cow = new Stats(1.2f, 2f, 32f);

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Stats pig = new Stats(1.2f, 2f, 32f);

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Stats sheep = new Stats(1.2f, 2f, 32f);

    public static class Stats {
        public Stats(float defaultSlowSpeed, float defaultFastSpeed, float defaultDistance) {
            this.slowSpeed = defaultSlowSpeed;
            this.fastSpeed = defaultFastSpeed;
            this.distance = defaultDistance;
        }

        @ConfigEntry.Gui.Tooltip(count=2)
        public float slowSpeed;

        @ConfigEntry.Gui.Tooltip(count=2)
        public float fastSpeed;

        @ConfigEntry.Gui.Tooltip(count=2)
        public float distance;
    }

    @Override
    public void validatePostLoad() throws ValidationException {

    }

    public static ARFYModConfig getConfig() {
        return AutoConfig.getConfigHolder(ARFYModConfig.class).getConfig();
    }
}
