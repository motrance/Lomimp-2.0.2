package coffeemunchingmonkeys.tools.lomimp.bricks;

/**
 *
 * Lomimp
 * bricks.Game
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public enum Game {
    ALL3("All 3 games"),
    VIKING("Viking"),
    EUROJACKPOT("EuroJackpot"),
    Lotto("Lotto");

    private String displayName;

    Game(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }

    // Optionally and/or additionally, toString.
    @Override public String toString() { return displayName; }
}
