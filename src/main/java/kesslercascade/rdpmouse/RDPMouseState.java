package kesslercascade.rdpmouse;

public final class RDPMouseState {
    private RDPMouseState() {}

    public static volatile boolean enabled = true;
    public static volatile double sensitivityMultiplier = 1.75;

    public static final double UNSET = Double.MIN_VALUE;

    public static volatile double lastX = UNSET;
    public static volatile double lastY = UNSET;
    public static volatile boolean justRecenter = false;
    public static volatile double recenterTargetX = UNSET;
    public static volatile double recenterTargetY = UNSET;

    /** Camera pan deltas injected by keyboard pan keys each tick. */
    public static volatile double panDX = 0;
    public static volatile double panDY = 0;

    public static void reset() {
        lastX = UNSET;
        lastY = UNSET;
        justRecenter = false;
        recenterTargetX = UNSET;
        recenterTargetY = UNSET;
        panDX = 0;
        panDY = 0;
    }
}
