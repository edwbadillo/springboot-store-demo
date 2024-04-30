package com.edwbadillo.storedemo.auth.jwt;

import com.edwbadillo.storedemo.auth.JWT;
import com.edwbadillo.storedemo.auth.dto.JWTResponse;
import com.edwbadillo.storedemo.customer.Customer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;


/**
 * Service for managing JWT.
 *
 * @author edwbadillo
 */
@Service
public class JwtService {

    /**
     * The secret key used to sign JWT tokens,
     * must be large to obtain a key byte array with >= 256 bits or else it will fail.
     * <p>
     * example: GFwVYS1I0m1YIVG7ICaSKT4c50tlAvnJB144C384BBED7286B2654454E28C7
     */
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-minutes}")
    private int jwtExpirationMinutes;

    /**
     * Create a JWT token for a customer.
     *
     * @param customer the customer
     * @return the token
     */
    public JWTResponse getToken(Customer customer) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JWT.ROLE_CLAIM, JWT.CUSTOMER_ROLE);
        claims.put(JWT.TOKEN_TYPE_CLAIM, JWT.ACCESS_TOKEN);
        String jwt = generateToken(String.valueOf(customer.getId()), claims);
        return new JWTResponse(jwt);
    }

    private String generateToken(String subject, HashMap<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + 1000L * 60 * jwtExpirationMinutes))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
