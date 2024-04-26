package main.java.com.turbomeme.servlet;

/**
 * Used for informing about malformed or invalid data when creating a meme.
 */
public final class InvalidInputException extends Exception
{
  public InvalidInputException(final String message)
  {
    super(message);
  }

  public InvalidInputException(final String message, final Throwable cause)
  {
    super(message, cause);
  }
}
