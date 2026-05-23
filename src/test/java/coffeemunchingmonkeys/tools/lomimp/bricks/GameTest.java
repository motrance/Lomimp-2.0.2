package coffeemunchingmonkeys.tools.lomimp.bricks;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {

    @Test
    public void displayNameReturnsConfiguredLabels() {
        Assert.assertEquals("All 3 games", Game.ALL3.displayName());
        Assert.assertEquals("Viking", Game.VIKING.displayName());
        Assert.assertEquals("EuroJackpot", Game.EUROJACKPOT.displayName());
        Assert.assertEquals("Lotto", Game.Lotto.displayName());
    }

    @Test
    public void toStringReturnsDisplayName() {
        Assert.assertEquals("All 3 games", Game.ALL3.toString());
        Assert.assertEquals("Viking", Game.VIKING.toString());
        Assert.assertEquals("EuroJackpot", Game.EUROJACKPOT.toString());
        Assert.assertEquals("Lotto", Game.Lotto.toString());
    }
}
