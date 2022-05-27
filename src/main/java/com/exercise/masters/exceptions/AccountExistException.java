/**
 * 
 */
package com.exercise.masters.exceptions;

/**
 * @author michaeldelacruz
 *
 */
public class AccountExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountExistException(String message) {
		super(message);
	}
	
	public AccountExistException(String message, Throwable t) {
		super(message, t);
	}
	
}
