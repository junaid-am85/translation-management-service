package com.java.translation.controller;

import com.java.translation.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	public static final String USERNAME = "admin";

	public static final String PASSWORD = "admin";

	private final JwtUtil jwtUtil;

	@Autowired
	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

		if (USERNAME.equals(username) && PASSWORD.equals(password)) {
			String token = jwtUtil.generateToken(username);

			return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
		}

		return ResponseEntity.status(401).body("Invalid credentials");
	}
}
