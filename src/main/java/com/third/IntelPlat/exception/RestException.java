package com.third.IntelPlat.exception;

public class RestException extends RuntimeException {
	  static final long serialVersionUID = 1L;

	  /**
	   * Creates exception with the specified message. 
	   *
	   * @param msg error message describing a possible cause of this exception.
	   */
	  public RestException(String msg) {
	    super(msg);
	  }

	  /**
	   * Creates exception with the specified message and cause.
	   *
	   * @param msg error message describing what happened.
	   * @param cause root exception that caused this exception to be thrown.
	   */
	  public RestException(String msg, Throwable cause) {
	    super(msg, cause);
	  }

	  /**
	   * Creates exception with the specified cause. 
	   * @param cause root exception that caused this exception to be thrown.
	   */
	  public RestException(Throwable cause) {
	    super(cause);
	  }
}