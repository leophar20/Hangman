public class EmptyFieldException extends Exception
{
    public EmptyFieldException() {
    	super("Need to have atleast 1 character.");
    }
}
