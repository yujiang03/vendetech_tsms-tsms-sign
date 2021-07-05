package com.vendetech.project.tsms;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@RequestMapping("hello")
	String hello() {
		return "Hello World!";
	}

	@RequestMapping("dbop")
	@ExceptionHandler
	String dataOperation() {
		return "Hello World.";
	}

	@ExceptionHandler
	public String doError(Exception ex) throws Exception {
		ex.printStackTrace();
		return ex.getMessage();
	}
}
