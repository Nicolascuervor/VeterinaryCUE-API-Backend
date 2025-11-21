package co.cue.auth.models.entities;
import co.cue.auth.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 100)
        private String nombre;

        @Column(nullable = false, length = 100)
        private String apellido;

        @Column(nullable = false, unique = true, length = 150)
        @Email(message = "El formato del correo electrónico es inválido (debe contener @ y dominio)")
        private String correo;

        @Column(nullable = false)
        private String contrasenia;

        @Column(length = 200)
        private String direccion;

        @Column(length = 20)
        private String telefono;


        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private Role userRole;

        @Column(nullable = false)
        private boolean activo;


        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(nullable = false)
        private LocalDateTime updatedAt;

    public Usuario(String nombre, String apellido, String correo, String contrasenia, String direccion, String telefono, Role userRole) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.direccion = direccion;
        this.telefono = telefono;
        this.userRole = userRole;
        this.activo = true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return this.contrasenia;
    }

    @Override
    public String getUsername() {
        return this.correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;

    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.activo;
    }

}
