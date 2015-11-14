package com.grimsmiler.Helpers.Logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ilya on 13/11/15.
 */
public class LoggingHelper {
    private static final Logger logger = Logger.getGlobal();

    public static void logError(Exception e) {

        String msg = e.getMessage() + "\n<----------------->\n" + e.getStackTrace();e.getClass();

        logger.logp(Level.SEVERE,e.getClass().getName(),"",msg,e);
    }

    public static void logInfo(String msg) {
        logger.log(Level.INFO,msg);
    }

    public LoggingHelper getLoggerHelper() {
        return this;
    }
}
