package kafkatests.handleExeption;

public class HandlerExeptionLimit extends RuntimeException {
    public HandlerExeptionLimit(String messException, String anyValue) {
        super(String.join(" ", messException,  anyValue));
    }
}
