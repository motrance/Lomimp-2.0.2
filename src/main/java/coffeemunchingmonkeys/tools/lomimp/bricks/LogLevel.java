package coffeemunchingmonkeys.tools.lomimp.bricks;

/**
 *
 * Lomimp
 * bricks.LogLevel
 * 
 * @version 2.0.2
 * @since 2026-05-15
 **/
public enum LogLevel {
    //Logleve
    //   0 = no log
    //   1 = info
    //   2 = errors
    //   3 = warnings
    //   4 = exceptions
    //   5 = debug

    None(0),
    Info(1),
    Error(2),
    Warning(3),
    Exceptions(4),
    Debug(5);

    private Integer logLevel;

    LogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Integer logLevel() {
        return logLevel;
    }

    @Override public String toString() {
        return String.valueOf(logLevel != null ? logLevel : 1);
    } 
}
