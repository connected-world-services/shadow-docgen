package com.connectedworldservices.docgen.utils;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.function.Supplier;

/**
 * Utility class to audit logs
 *
 */
public class LoggerUtils {

    private static final Marker MARKER_STASH = MarkerFactory.getMarker("stash");

    public enum Level {
        TRACE, DEBUG, WARN, INFO, ERROR
    }


    /**
     * Audit logs for INFO level
     * @param logger
     * @param event
     * @param cwsRef
     * @param logData
     */
    public static void auditInfo(Logger logger, String event, String cwsRef, Supplier<String> logData) {
        audit(logger, Level.INFO, event, cwsRef, logData);
    }


    /**
     * Audit logs for ERROR level
     * @param logger
     * @param event
     * @param cwsRef
     * @param logData
     */
    public static void auditError(Logger logger, String event, String cwsRef, Supplier<String> logData) {
        audit(logger, Level.ERROR, event, cwsRef, logData);
    }

    private static void audit(Logger logger, Level level, String event, String cwsRef, Supplier<String> logData) {
        auditLevel(logger, level, event, cwsRef, logData);
    }

    private static void auditLevel(Logger logger, Level level, String event, String cwsRef,  Supplier<String> logData) {
        MDC.put("event", event);
        MDC.put("cwsRef", cwsRef);
        log(logger, level, logData);
        MDC.remove("event");
        MDC.remove("cwsRef");
    }

    public static void log(Logger logger, Level level, Supplier<String> fullData) {
        if (logger != null && level != null) {
            switch (level) {
                case TRACE:
                    if (logger.isTraceEnabled()) {
                        logger.trace(MARKER_STASH, fullData.get());
                    }
                    break;
                case DEBUG:
                    if (logger.isDebugEnabled()) {
                        logger.debug(MARKER_STASH, fullData.get());
                    }
                    break;
                case INFO:
                    if (logger.isInfoEnabled()) {
                        logger.info(MARKER_STASH, fullData.get());
                    }
                    break;
                case WARN:
                    if (logger.isWarnEnabled()) {
                        logger.warn(MARKER_STASH, fullData.get());
                    }
                    break;
                case ERROR:
                    if (logger.isErrorEnabled()) {
                        logger.error(MARKER_STASH, fullData.get());
                    }
                    break;
            }
        }
    }
}
