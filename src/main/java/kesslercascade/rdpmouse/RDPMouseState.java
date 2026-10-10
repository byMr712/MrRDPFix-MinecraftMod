package kesslercascade.rdpmouse;

public final class RDPMouseState {
    private RDPMouseState() {}

    public static volatile boolean enabled = true;
    public static volatile double sensitivityMultiplier = 1.5;

    /** Camera pan deltas injected by keyboard pan keys each tick. */
    public static volatile double panDX = 0;
    public static volatile double panDY = 0;

    public static void reset() {
        panDX = 0;
        panDY = 0;
    }
}
