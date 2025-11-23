import java.util.Random;

/**
 * Factory for producing random Weather events.
 */
public class WeatherFactory {
    private static final Random RNG = new Random();

    /**
     * Returns a random Weather instance (uniform among the types).
     */
    public static Weather randomWeather() {
        int n = RNG.nextInt(4);
        switch (n) {
            case 0: return new Hurricane();
            case 1: return new Tornado();
            case 2: return new Tsunami();
            default: return new AcidRain();
        }
    }
}
