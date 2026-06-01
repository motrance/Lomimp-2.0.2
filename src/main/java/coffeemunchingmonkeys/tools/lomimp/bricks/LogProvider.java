package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JTextArea;
import static java.nio.file.StandardOpenOption.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * Lomimp
 * bricks.Storage
 * 
 * Loglevel - 0 for no log, 1 for info, 2 for errors, 3 for warnings, 4 for exceptions and 5 for debug
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class LogProvider {
    //Fields
    private String fileName = "log.txt";
    private Path logPath = null;
    private static LogLevel logLevel = LogLevel.Debug;
    static 	boolean printToConsole = true;
    private FileChannel channel = null;
    private JTextArea jTextArea;

    public LogProvider() {
    }

    public LogProvider(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    private void initFile() {
        try {
            Files.createDirectories(logPath.getParent());

            channel = FileChannel.open(
                    logPath,
                    CREATE,
                    WRITE,
                    APPEND
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * @param msg
     */
    private synchronized void log(String msg, Integer severity) {
        if(channel  == null) {
            initFile();
        }
        try {
            LogLevel logLevelString = LogLevel.values()[(severity!=null && severity >=0 && severity < LogLevel.values().length)? severity : LogLevel.Debug.logLevel()];

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss:SSS");
            String formatted = now.format(formatter);
            String line = formatted + "\t" + logLevelString.name() + "\t" + msg + System.lineSeparator();
            ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());

            // 1. REALTIME FILE WRITE
            channel.write(buffer);
            channel.force(true);

            // 2. TERMINAL (NO TIMESTAMP, NO SEVERITY)
            if(printToConsole)
                System.out.println(formatted + "\t" + logLevelString.name() + "\t" + msg);

            // 3. GUI (NO TIMESTAMP, NO SEVERITY)
            if (severity != null && severity == 1) {
                printToGui(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * @param msg
     */
    public void printToGui(String msg) {
            if (jTextArea != null) {
                jTextArea.append(msg + "\n");
            }
    }

    /** 
     * @param msg
     */
    //Loglevel - 0 for no log, 1 for info, 2 for errors, 3 for warnings, 4 for exceptions and 5 for debug
    public void writeInfo(String msg) {
        if (logLevel.logLevel() >= LogLevel.Info.logLevel()) {
            log(msg, LogLevel.Info.logLevel());
        }

    }
    
    /** 
     * @param msg
     */
    public void writeError(String msg) {
        if (logLevel.logLevel() >= LogLevel.Error.logLevel()) {
            log(msg, LogLevel.Error.logLevel());
        }
    }

    /** 
     * @param msg
     */
    public void writeWarning(String msg) {
        if (logLevel.logLevel() >= LogLevel.Warning.logLevel()) {
            log(msg, LogLevel.Warning.logLevel());
        }
    }

    /** 
     * @param e
     */
    public void writeException(Exception e) {
        if (logLevel.logLevel() >= LogLevel.Exceptions.logLevel()) {
            log(stack2string(e), LogLevel.Exceptions.logLevel());
        }
    }

    /** 
     * @param msg
     */
    public void writeDebug(String msg) {
        if (logLevel.logLevel() >= LogLevel.Debug.logLevel()) {
            log(msg, LogLevel.Debug.logLevel());
        }
    }

    /** 
     * @param exception
     * @return String
     */
    private String stack2string(Exception exception) {
        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            exception.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }

    /**
     * set the log level of the logfile mechanism
     *
     * @param level LogLevel
     */
    public void setLogLevel(LogLevel level)
    {
        logLevel = level;
    }

    /**
     * set the log level of the logfile mechanism
     *
     * @param level int, 0 for no log, 1 for exceptions only, 2 for error, 3 for warning, 4 for info and 5 for debug
     */
    public void setLogLevel(Integer level)
    {
        logLevel = LogLevel.values()[(level!=null && level >=0 && level < LogLevel.values().length)? level : LogLevel.Debug.logLevel()];
    }

    /** 
     * @param logLevelStgring
     */
    public void setlogPath(String logPath) {
        if(logPath != null && logPath.length() > 0) {
            this.logPath = Paths.get(logPath + "/" + fileName);
        }
    }

    
    /** 
     * @param logLevelStgring
     */
    public void setLogLevel(String logLevelStgring) {
        if(logLevelStgring != null && logLevelStgring.length() > 0) {
            logLevel = LogLevel.valueOf(logLevelStgring);
            //this.logLevel = logLevel;
        }
    }

    /**
     * decide if each log entry will be printed on the console
     *
     * @param state boolean, true for statements will be printed, false for not
     */
    public void setPrintToConsole(boolean state)
    {
        printToConsole = state;
    }
}
