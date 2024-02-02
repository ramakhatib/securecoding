package ramafinal;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
 
public class MyLogger {
    final static Logger LOGGER = Logger.getLogger("MyLog");
 
    static {
        try {
            FileHandler fh = new FileHandler(System.getProperty("user.home") + System.getProperty("file.separator") + "logfile.log", true);
            LOGGER.addHandler(fh);
            String userHome = System.getProperty("user.home");
            System.out.println("User's Home Directory: " + userHome);
 
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }
 
    public static void writeToLog(String msg) {
        LOGGER.log(Level.INFO, msg);
    }
 
    public static void writeToLog(String msg, Exception e) {
        LOGGER.log(Level.WARNING, msg, e);
    }
}
 
 
 
// LOGGER.setUseParentHandlers(true);
//fh= new FileHandler(System.getProperty("user.home")+System.getProperty("file.separator")+"alog.log",true);