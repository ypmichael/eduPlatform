package com.third.IntelPlat.exception;

public class ServiceException extends RuntimeException {
	  static final long serialVersionUID = 1L;

	  /**
	   * Creates exception with the specified message. 
	   *
	   * @param msg error message describing a possible cause of this exception.
	   */
	  public ServiceException(String msg) {
	    super(msg);
	  }

	  /**
	   * Creates exception with the specified message and cause.
	   *
	   * @param msg error message describing what happened.
	   * @param cause root exception that caused this exception to be thrown.
	   */
	  public ServiceException(String msg, Throwable cause) {
	    super(msg, cause);
	  }

	  /**
	   * Creates exception with the specified cause. 
	   * @param cause root exception that caused this exception to be thrown.
	   */
	  public ServiceException(Throwable cause) {
	    super(cause);
	  }
}