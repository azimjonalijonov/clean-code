package uz.market.backend.web.rest;
import org.springframework.security.core.Authentication;
import uz.market.backend.domain.User;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.market.backend.repository.UserRepository;
import uz.market.backend.security.JwtTokenProvider;
import uz.market.backend.web.rest.vm.LoginVm;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserJwtController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserJwtController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

        this.userRepository = userRepository;
    }
    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginVm loginVm){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVm.getUsername(),loginVm.getPassword()));
        User user =userRepository.findByLogin(loginVm.getUsername());
        if (user==null){
            throw new UsernameNotFoundException("user not found");
        }
        String token =jwtTokenProvider.create(user.getUsername(),  user.getRoles());
        Map<Object,Object> map =new HashMap<>();
        map.put("username",user.getUsername());
        map.put("token",token);
        return ResponseEntity.ok(map);

















    }

}
