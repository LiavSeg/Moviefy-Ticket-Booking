package moviefyPackge.moviefy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Wrapper utility for standardized and consistent logging.
 * Each instance is bound to a specific controller or component class
 * and prefixes logs with a descriptive context label.
 * Typical usage includes logging lifecycle events, transformations,
 * custom messages, warnings, and errors.
 * @param <T> the class type using the logger (e.g., a controller class)
 */
public class LoggerWrapper<T> {

    private  final Logger logger;
    private final String message ;
    private final String name;

    /**
     * Initializes the logger for the specified class and context name.
     * @param cls the class that owns the logger
     * @param name a short label for logging context (e.g., "Booking", "XML Handler")
     */
    public LoggerWrapper (Class<T> cls, String name){
        this.name = name;
        this.logger = LoggerFactory.getLogger(cls);
        this.message = String.format("%s Client Request: ", name);
    }
    /**
     * Logs a general information message with contextual prefix.
     * @param description the message content to log
     */
    public void LogInfo(String description){
        logger.info("{}{}", this.message, description);
    }

    /**Logs a "SUCCESS" message*/
    public void success(){
        logger.info("{}SUCCESS", this.message);
    }

    /**Logs a message indicating the component is initialized and ready*/
    public void startLog(){
        logger.info("{}  Endpoint is set and ready to operate.", name);
    }
    /**
     * Logs a custom message without any prefix.
     * @param costumeMsg the raw message to log
     */
    public void costume(String costumeMsg){
        logger.info(costumeMsg);
    }

    /**Logs a DTO mapping step*/
    public void fromDTO(){
        logger.info("Mapping from Entity to DTO");
    }

    /**Logs an Entity mapping step*/
    public void fromEntity(){
        logger.info("Mapping from DTO to Entity");
    }
    /**
     * Logs a warning message.
     * @param warning the warning content
     */
    public void warn(String warning){logger.warn(warning);}
    public void error(String error){logger.error(error);}
    public void service(String info){logger.info(info);}
}
