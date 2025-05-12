package com.java.translation.controller;

import com.java.translation.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Value("${auth.username:admin}")
	private String name;

	@Value("${auth.password:admin}")
	private String pass;

	private final JwtUtil jwtUtil;

	@Autowired
	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

		if (name.equals(username) && pass.equals(password)) {
			String token = jwtUtil.generateToken(username);

			return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
		}

		return ResponseEntity.status(401).body("Invalid credentials");
	}
}
