package client;

/**
 * Created by hugo on 11/17/14.
 */
public class ArgumentValidator {

    public ArgumentValidator() {

    }

    public void validateProgramArguments(String[] arguments) throws IllegalArgumentException {
        if(arguments.length > 4) {
            throw new IllegalArgumentException();
        }
    }

    public void validateGetArguments(String[] arguments) throws IllegalArgumentException {
        if(arguments.length != 1) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSearchArguments(String[] arguments) throws IllegalArgumentException {
        if(arguments.length != 1) {
            throw new IllegalArgumentException();
        } else if (arguments[1].length() < 1 || arguments[1].length() > 32) {
            throw new IllegalArgumentException();
        }
    }

    public void validatePostArguments(String[] arguments) throws IllegalArgumentException {
        if(arguments.length != 5) {
            throw new IllegalArgumentException("Error: Need to supply atleast 2 arguments.");
        }
    }

}
