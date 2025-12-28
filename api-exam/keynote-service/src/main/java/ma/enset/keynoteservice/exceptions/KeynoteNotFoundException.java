package ma.enset.keynoteservice.exceptions;

public class KeynoteNotFoundException extends RuntimeException {
    public KeynoteNotFoundException(String message) {
        super(message);
    }
}
