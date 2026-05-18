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
 * Loglevel - 0 for no log, 1 for exceptions only, 2 for error, 3 for warning, 4 for info and 5 for debug
 *
 * @version 2.0.2
 * @since 2026-05-15
 */
public class LogProvider {
    //Fields
    private final Path logPath = Paths.get("logs/log.txt");
    private static LogLevel logLevel = LogLevel.DEBUG;
    static 	boolean printToConsole = true;
    private FileChannel channel;
    private JTextArea jTextArea;

    public LogProvider() {
        initFile();
    }

    public LogProvider(JTextArea jTextArea) {
        initFile();
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
    private synchronized void log(String msg, String severity) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss:SSS");
            String formatted = now.format(formatter);

            String line = formatted + "\t" + severity + "\t" + msg + System.lineSeparator();
            ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());

            // 1. REALTIME FILE WRITE
            channel.write(buffer);
            channel.force(true);

            // 2. TERMINAL (NO TIMESTAMP, NO SEVERITY)
            if(printToConsole)
                System.out.println(formatted + "\t" + severity + "\t" + msg);

            // 3. GUI (NO TIMESTAMP, NO SEVERITY)
            printToGui(msg);

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
    public void writeException(Exception e) {
        if (logLevel.logLevel() >= LogLevel.EXCEPTIONS.logLevel()) {
            log(stack2string(e), "Exception");
        }
    }
    
    public void writeError(String msg) {
        if (logLevel.logLevel() >= LogLevel.ERROR.logLevel()) {
            log(msg, "Error");
        }
    }
    
    public void writeWarning(String msg) {
        if (logLevel.logLevel() >= LogLevel.WARNING.logLevel()) {
            log(msg, "Warning");
        }
    }

    public void writeInfo(String msg) {
        if (logLevel.logLevel() >= LogLevel.INFO.logLevel()) {
            log(msg, "Info");
        }

    }

    public void writeDebug(String msg) {
        if (logLevel.logLevel() >= LogLevel.DEBUG.logLevel()) {
            log(msg, "Debug");
        } else {
            printToGui(msg);
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
        logLevel = LogLevel.values()[(level!=null && level >=0 && level < LogLevel.values().length)? level : LogLevel.DEBUG.logLevel()];
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
