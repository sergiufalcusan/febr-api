package io.febr.api.service;

import io.febr.api.dto.AuthDTO;
import io.febr.api.mapper.UserMapper;
import io.febr.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
    private final UserRepository userRepository;
    private JwtEncoder jwtEncoder;
    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;

    /**
     * Generate token for user
     *
     * @param userLogin user login request
     * @return token
     */
    public String generateToken(AuthDTO.LoginRequest userLogin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.username(),
                        userLogin.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Token requested for user :{}", authentication.getAuthorities());

        Instant now = Instant.now();
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Get current user
     *
     * @return current user
     */
    public AuthDTO.UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return userMapper.toDto(user);
    }
}
