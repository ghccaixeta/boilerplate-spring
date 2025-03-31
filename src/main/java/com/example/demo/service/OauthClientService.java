package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entities.OAuthClient;
import com.example.demo.repository.OauthClientRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OauthClientService {

  private final OauthClientRepository oauthClientRepository;

  public Optional<OAuthClient> getByClientID(String clientId) {
    return oauthClientRepository.findByClientId(clientId);
  }

}
