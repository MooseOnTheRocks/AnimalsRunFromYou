package dev.foltz.animalsrunfromyou;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "animals-run-from-you")
public class ARFYModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public AnimalStats chicken = new AnimalStats(1.5f, 2.5f, 32, 0.0f, false);

    @ConfigEntry.Gui.CollapsibleObject
    public AnimalStats cow = new AnimalStats(1.2f, 2f, 48, 0.0f, false);

    @ConfigEntry.Gui.CollapsibleObject
    public AnimalStats pig = new AnimalStats(1.4f, 2f, 48, 0.0f, false);

    @ConfigEntry.Gui.CollapsibleObject
    public AnimalStats sheep = new AnimalStats(1.0f, 2f, 48, 0.0f, true);

    public static class AnimalStats {
        public AnimalStats(float defaultFarSpeed, float defaultNearSpeed, int defaultDistance, float defaultRatio, boolean defaultRunFromWolves) {
            this.farSpeed = defaultFarSpeed;
            this.nearSpeed = defaultNearSpeed;
            this.distance = defaultDistance;
            this.ratio = defaultRatio;
            this.runFromWolves = defaultRunFromWolves;
        }

        @ConfigEntry.Gui.Tooltip(count=3)
        public float farSpeed;

        @ConfigEntry.Gui.Tooltip(count=3)
        public float nearSpeed;

        @ConfigEntry.Gui.Tooltip(count=4)
        @ConfigEntry.BoundedDiscrete(max=128)
        public int distance;

        @ConfigEntry.Gui.Tooltip(count=4)
        public float ratio;

        @ConfigEntry.Gui.Tooltip(count=2)
        public boolean runFromWolves;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        correctStatsPostLoad(chicken);
        correctStatsPostLoad(cow);
        correctStatsPostLoad(pig);
        correctStatsPostLoad(sheep);
    }

    private void correctStatsPostLoad(AnimalStats animalStats) {
        if (animalStats.ratio < 0) {
            animalStats.ratio = 0;
        }
        else if (animalStats.ratio > 1) {
            animalStats.ratio = 1;
        }

        if (animalStats.distance < 0) {
            animalStats.distance = 0;
        }
        else if (animalStats.distance > 128) {
            animalStats.distance = 128;
        }

        if (animalStats.farSpeed < 0) {
            animalStats.farSpeed = 0;
        }
        else if (animalStats.farSpeed > 10) {
            animalStats.farSpeed = 10;
        }

        if (animalStats.nearSpeed < 0) {
            animalStats.nearSpeed = 0;
        }
        else if (animalStats.nearSpeed > 10) {
            animalStats.nearSpeed = 10;
        }
    }

    public static ARFYModConfig getConfig() {
        return AutoConfig.getConfigHolder(ARFYModConfig.class).getConfig();
    }
}
