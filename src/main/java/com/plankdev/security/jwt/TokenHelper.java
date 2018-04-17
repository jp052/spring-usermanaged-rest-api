package com.plankdev.security.jwt;

import com.plankdev.security.dataaccess.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenHelper {
    @Value("${app.name}")
    private String appName;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expires_in}")
    private int expiresIn;

    @Value("${jwt.header}")
    private String authHeader;

    private static final String AUDIENCE_UNKNOWN = "unknown";
    
    private final String API_APP_KEY = "apiapp";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            e.printStackTrace();
        }
        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }
    
    public String getAppnameFromToken(String token) {
    	String apiApp;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            apiApp = claims.get(API_APP_KEY, String.class);
        } catch (Exception e) {
            apiApp = null;
        }
        return apiApp;
	}

    public String refreshToken(String token) {
        String refreshedToken;
        Date a = new Date();
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            claims.setIssuedAt(a);
            refreshedToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate())
                    .signWith(SIGNATURE_ALGORITHM, secret)
                    .compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String generateToken(String username) {
        String audience = generateAudience();
        Date generateExpirationDate = generateExpirationDate();
		return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .setAudience(audience)              
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }
    
    public String generateToken(String username, String apiapp) {
        String audience = generateAudience();
        Date generateExpirationDate = generateExpirationDate();
		return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .claim(API_APP_KEY, apiapp)
                .setAudience(audience)              
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    private String generateAudience() {
        String audience = AUDIENCE_UNKNOWN;
        return audience;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
            e.printStackTrace();
        }
        return claims;
    }

    private Date generateExpirationDate() {
      
        return new Date(new Date().getTime() + (long) expiresIn * 1000);
    }

    public int getExpiredIn() {
        return expiresIn;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        AppUser appUser = (AppUser) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        return (
                username != null &&
                        username.equals(userDetails.getUsername()) &&
                        !isCreatedBeforeLastPasswordReset(created, appUser.getLastPasswordResetDate())
        );
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     *  Getting the token from Authentication header
     *  e.g Bearer your_token
     */
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(authHeader);
    }

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public void setExpiresIn(int expiresInSeconds) {
		this.expiresIn = expiresInSeconds;
	}

	public void setAuthHeader(String authHeader) {
		this.authHeader = authHeader;
	}
   
    
    

}
