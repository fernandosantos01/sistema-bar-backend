package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.dto.AuthenticationDTO;
import com.POO.Sistema_De_Bar.dto.LoginResponseDTO;
import com.POO.Sistema_De_Bar.dto.RegisterDTO;
import com.POO.Sistema_De_Bar.model.Usuario;
import com.POO.Sistema_De_Bar.repository.UsuarioRepository;
import com.POO.Sistema_De_Bar.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var auth = authenticationManager.authenticate(usernamePassword);

        Usuario user = (Usuario) auth.getPrincipal();
        assert user != null;
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}