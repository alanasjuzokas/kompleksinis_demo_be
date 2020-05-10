package bakalauras.demo.config;

import bakalauras.demo.entities.domain.Type;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtConfig {

   public static final String SIGN_KEY = "feY0ks5HO7yuN+QuujgC6T6BWAupXuD3ZC/3wRWE8wQ=";

   public static boolean isAdministrator(String token) {
      SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGN_KEY));

      String role = Jwts.parserBuilder().setSigningKey(key).build()
              .parseClaimsJws(token).getBody().get("role", String.class);

      return role.equals(Type.ADMINISTRATOR.toString());
   }

   public static boolean isRequester(String token) {
      SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGN_KEY));

      String role = Jwts.parserBuilder().setSigningKey(key).build()
              .parseClaimsJws(token).getBody().get("role", String.class);

      return role.equals(Type.REQUESTER.toString());
   }

   public static String getRole(String token) {
      SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGN_KEY));

      return Jwts.parserBuilder().setSigningKey(key).build()
              .parseClaimsJws(token).getBody().get("role", String.class);
   }

}
