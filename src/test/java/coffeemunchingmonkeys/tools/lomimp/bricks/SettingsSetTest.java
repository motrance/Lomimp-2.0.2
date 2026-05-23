package coffeemunchingmonkeys.tools.lomimp.bricks;

import org.junit.Assert;
import org.junit.Test;

public class SettingsSetTest {

    @Test
    public void gettersReturnAssignedFieldValues() {
        SettingsSet settingsSet = new SettingsSet();
        settingsSet.initialYear = 2025;
        settingsSet.url = "https://example.test/viking";
        settingsSet.numberSize = 6;
        settingsSet.numberMax = 48;
        settingsSet.extrasize = 2;
        settingsSet.extraMax = 10;

        Assert.assertEquals(2025, settingsSet.getInitialYear());
        Assert.assertEquals("https://example.test/viking", settingsSet.getUrl());
        Assert.assertEquals(6, settingsSet.getNumberSize());
        Assert.assertEquals(48, settingsSet.getNumberMax());
        Assert.assertEquals(2, settingsSet.getExtrasize());
        Assert.assertEquals(10, settingsSet.getExtraMax());
    }

    @Test
    public void defaultValuesAreZeroAndNull() {
        SettingsSet settingsSet = new SettingsSet();

        Assert.assertEquals(0, settingsSet.getInitialYear());
        Assert.assertNull(settingsSet.getUrl());
        Assert.assertEquals(0, settingsSet.getNumberSize());
        Assert.assertEquals(0, settingsSet.getNumberMax());
        Assert.assertEquals(0, settingsSet.getExtrasize());
        Assert.assertEquals(0, settingsSet.getExtraMax());
    }
}
