package se.persandstrom.highscoretracker.exception;


import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;

public abstract class AbstractException extends Exception {

    private static final String JSON_ERROR_CODE_KEY = "errorCode";
    private static final String JSON_ERROR_NAME_KEY = "errorName";
    private static final String JSON_MESSAGE_KEY = "message";

    private final int statusCode;
    private final int errorCode;
    private final String errorName;

    AbstractException(HttpStatus statusCode, int errorCode, String message) {
        super(message);
        this.statusCode = statusCode.value();
        this.errorCode = errorCode;
        this.errorName = ErrorCodes.ERROR_NAME.get(errorCode);
    }

    public String getJsonAsString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JSON_ERROR_CODE_KEY, errorCode);
        jsonObject.addProperty(JSON_ERROR_NAME_KEY, errorName);
        jsonObject.addProperty(JSON_MESSAGE_KEY, getMessage());
        return jsonObject.toString();
    }

    public int getStatusCode() {
        return statusCode;
    }
}
