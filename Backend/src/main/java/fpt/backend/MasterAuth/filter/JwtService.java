package fpt.backend.MasterAuth.filter;

public interface JwtService {
    String extractUsername(String token);

    String generateToken(String username, long expiredTime);

    boolean isValidToken(String token);
}
