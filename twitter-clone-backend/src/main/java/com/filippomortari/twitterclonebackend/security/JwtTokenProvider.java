package com.filippomortari.twitterclonebackend.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private UserDetails loadUserByUsername(String username) {
        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password("")//
                .authorities("ROLE_USER")//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }

    public String getUsername(String token) { // not validating really at this stage as it got past API gateway where jwt validation already occurred. this is more to fetch the username
        DecodedJWT decode = JWT.decode(token);
        String username = decode.getClaim("username").asString();
        if (StringUtils.isBlank(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return username;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }



}
