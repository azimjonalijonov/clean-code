package uz.market.backend.security;
//import io.jsonwebtoken.*;
//
import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import uz.market.backend.domain.Role;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Base64;
//import java.util.Date;
//import java.util.Set;
//
//@Component
//public class JwtTokenProvider {
//    private final UserDetailsService userDetailsService;
//    @Value("${jwt.token.secret}")
//    private  String secret;
//    @Value("${jwt.token.validity}")
//    private Long validityMilliySeconds;
//
//
//    public JwtTokenProvider(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//        BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder();
//        return bCryptPasswordEncoder;
//    }
//    @PostConstruct
//    protected void init(){
//        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
//    }
//  public String create(String userName, Set<Role> roles){
//        Claims claims = Jwts.claims().setSubject(userName);
//        claims.put("roles",roles);
//      Date date =new Date();
//      Date validity =new Date(date.getTime()+validityMilliySeconds);
//      return Jwts.builder()
//              .setClaims(claims)
//              .setIssuedAt(date)
//              .setExpiration(validity)
//              .signWith(SignatureAlgorithm.HS256,this.secret)
//              .compact();
//
//  }
//  public boolean validateToken(String token){
// Jws<Claims> claimsJws =Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
// if(claimsJws.getBody().getExpiration().before(new Date())){
//     return false;
// }
// return true;
//    }
//    public String resolveToken(HttpServletRequest request){
//        String bearerToken =request.getHeader("Authorization");
//        if(bearerToken!=null&&bearerToken.startsWith("bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//
//    }
//    public Authentication getAuthentication(String token){
//        UserDetails userDetails =this.userDetailsService.loadUserByUsername(getUser(token));
//        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
//    }
//private String getUser(String token){
//return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
//
//}
//
//}

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.market.backend.domain.Role;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.validity}")
    private Long validtyMilliSecunds;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String create(String userName, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validtyMilliSecunds);
        return Jwts.builder().
                setClaims(claims)
                .setIssuedAt(now).
                setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();

    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            if (claimsJws.getBody().getExpiration().before(new Date())) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }
        return true;
    }
    public String resolveToken(HttpServletRequest request){
        String bearerToken =request.getHeader("Authorization");
        if(bearerToken!=null&&bearerToken.startsWith("bearer ")){
            return bearerToken.substring(7);
        }
        return null;

    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder();

    return  bCryptPasswordEncoder;
    }
}
