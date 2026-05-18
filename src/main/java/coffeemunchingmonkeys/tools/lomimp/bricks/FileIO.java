package coffeemunchingmonkeys.tools.lomimp.bricks;

import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * Lomimp
 * bricks.FileIO
 *
 * @version 2.0.2
 * @since 2026-05-15
 **/
public class FileIO {
    //Fields
    private LogProvider			log;
    private FileOutputStream 	fileOutputStream;

    public FileIO(LogProvider log) {
        this.log = log;
    }

    /**
     *
     * @param workBook
     */
    public boolean write(XSSFWorkbook workBook, String fileName) {
        boolean state = true;

        try {
            this.fileOutputStream = new FileOutputStream(fileName + ".xlsx");

            workBook.write(this.fileOutputStream);
        }
        catch(Exception e) {
            log.writeException(e);

            state = false;
        }

        return state;
    }

    /**
     *
     * @param workBook
     */
    public boolean writAndCloseWrite(XSSFWorkbook workBook, String fileName) {
        boolean state = true;

        try {
            this.fileOutputStream = new FileOutputStream(fileName + ".xlsx");

            workBook.write(this.fileOutputStream);
            workBook.close();

            this.fileOutputStream.flush();
            this.fileOutputStream.close();
        }
        catch(Exception e) {
            log.writeException(e);

            state = false;
        }

        return state;
    }

    /**
     *
     * @param workBook
     */
    public boolean close(XSSFWorkbook workBook) {
        boolean state = true;

        try {
            workBook.close();

            this.fileOutputStream.flush();
            this.fileOutputStream.close();
        }
        catch(Exception e) {
            log.writeException(e);

            state = false;
        }

        return state;
    }
}
