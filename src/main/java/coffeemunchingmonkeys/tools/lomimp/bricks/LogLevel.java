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
    //   1 = exceptions only
    //   2 = errors
    //   3 = warnings
    //   4 = info
    //   5 = debug
    
    NONE(0),
    EXCEPTIONS(1),
    ERROR(2),
    WARNING(3),
    INFO(4),
    DEBUG(5);

    private Integer logLevel;

    LogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Integer logLevel() {
        return logLevel;
    }

    @Override public String toString() {
        return String.valueOf(logLevel != null ? logLevel : 0);
    } 
}
