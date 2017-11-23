package com.bskyb.internettv.failure_exception;

public class WrongArgumentException extends Exception {

	private String argument;
	public WrongArgumentException(String errorMessage, String argument) {
		super(errorMessage );
		this.argument = argument;
	}

	public String getArgument() {
		return argument;
	}
}
