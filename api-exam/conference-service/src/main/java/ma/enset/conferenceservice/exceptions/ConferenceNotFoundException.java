package ma.enset.conferenceservice.exceptions;

public class ConferenceNotFoundException extends RuntimeException {
    public ConferenceNotFoundException(String message) {
        super(message);
    }
}
