package entity;

import java.math.BigDecimal;

public class StressScenario {
    private final String name;
    private final String timeline;
    private final String description;
    private final BigDecimal shockPercentage; // e.g., -0.34 for -34%

    public StressScenario(String name, String timeline, String description, BigDecimal shockPercentage) {
        this.name = name;
        this.timeline = timeline;
        this.description = description;
        this.shockPercentage = shockPercentage;
    }

    public String getName() { return name; }
    public String getTimeline() { return timeline; }
    public String getDescription() { return description; }
    public BigDecimal getShockPercentage() { return shockPercentage; }
}