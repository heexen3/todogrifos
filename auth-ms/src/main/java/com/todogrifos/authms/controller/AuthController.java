package com.todogrifos.authms.controller;

import com.todogrifos.authms.dto.AuthRequest;
import com.todogrifos.authms.dto.AuthResponse;
import com.todogrifos.authms.model.Usuario;
import com.todogrifos.authms.repository.UsuarioRepository;
import com.todogrifos.authms.service.CustomUserDetailsService;
import com.todogrifos.authms.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar usuario",
            description = "Crea un nuevo usuario en el sistema, encripta su contraseña y asigna un rol por defecto si no se especifica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno al registrar usuario")
    })
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("USER"); // O "ROLE_USER" dependiendo de tu convención
        }
        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente con ID: " + nuevoUsuario.getId());
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con sus credenciales y genera un token JWT válido para acceso a los microservicios protegidos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, token JWT generado"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o solicitud mal formada"),
            @ApiResponse(responseCode = "401", description = "No autorizado, credenciales incorrectas"),
            @ApiResponse(responseCode = "500", description = "Error interno durante autenticación")
    })
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Valida credenciales contra la base de datos
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Obtiene el objeto UserDetails completo para generar el token
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}