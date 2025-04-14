package com.example.demo.controller;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.TokenResponseDto;
import com.example.demo.entities.Role;
import com.example.demo.repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class LoginController {
  private final JwtEncoder jwtEncoder;
  private final UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public LoginController(JwtEncoder jwtEncoder, UserRepository userRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.jwtEncoder = jwtEncoder;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;

  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
    var user = userRepository.findByUsername(loginRequest.username());

    if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
      throw new BadCredentialsException("user or passwrod is invalid!");
    }

    var now = Instant.now();
    var expiresIn = 300L;

    var scopes = user.get().getRoles()
        .stream()
        .map(Role::getName)
        .collect(Collectors.joining(" "));

    var claims = JwtClaimsSet.builder()
        .subject(user.get().getUserId().toString())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiresIn))
        .claim("scope", scopes)
        .build();

    var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return ResponseEntity.ok(new TokenResponseDto(jwtValue, expiresIn));
  }

}
