package ua.com.nure.fitnessassistant.exeption;



public class EmptyDataException extends RuntimeException{
    private final String message;

    public EmptyDataException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
