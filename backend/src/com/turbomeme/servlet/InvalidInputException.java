package com.turbomeme.servlet;

public final class InvalidInputException extends Exception
{
  public InvalidInputException(final String message)
  {
    super(message);
  }

  public InvalidInputException(final Throwable cause)
  {
    super(cause);
  }

  public InvalidInputException(final String message, final Throwable cause)
  {
    super(message, cause);
  }
}