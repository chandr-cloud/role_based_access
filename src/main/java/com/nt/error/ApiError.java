package com.nt.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {

	private LocalDateTime timeStamp;
	private String error;
	private HttpStatus statusCode;

	public ApiError() {
		this.timeStamp = LocalDateTime.now();
	}

	public ApiError(String error, HttpStatus statusCode) {
		this();
		this.error = error;
		this.statusCode = statusCode;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public String getError() {
		return error;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}
}
