package coffeemunchingmonkeys.tools.lomimp.bricks;

import org.junit.Assert;
import org.junit.Test;

public class LogLevelTest {

    @Test
    public void logLevelReturnsConfiguredNumericValue() {
        Assert.assertEquals(Integer.valueOf(0), LogLevel.None.logLevel());
        Assert.assertEquals(Integer.valueOf(1), LogLevel.Info.logLevel());
        Assert.assertEquals(Integer.valueOf(2), LogLevel.Error.logLevel());
        Assert.assertEquals(Integer.valueOf(3), LogLevel.Warning.logLevel());
        Assert.assertEquals(Integer.valueOf(4), LogLevel.Exceptions.logLevel());
        Assert.assertEquals(Integer.valueOf(5), LogLevel.Debug.logLevel());
    }

    @Test
    public void toStringReturnsNumericRepresentation() {
        Assert.assertEquals("0", LogLevel.None.toString());
        Assert.assertEquals("1", LogLevel.Info.toString());
        Assert.assertEquals("2", LogLevel.Error.toString());
        Assert.assertEquals("3", LogLevel.Warning.toString());
        Assert.assertEquals("4", LogLevel.Exceptions.toString());
        Assert.assertEquals("5", LogLevel.Debug.toString());
    }
}
