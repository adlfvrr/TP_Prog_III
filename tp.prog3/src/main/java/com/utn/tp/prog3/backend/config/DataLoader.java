package com.utn.tp.prog3.backend.config;

import com.utn.tp.prog3.backend.model.ROLE;
import com.utn.tp.prog3.backend.model.User;
import com.utn.tp.prog3.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    //Cargaremos unos usuarios para probar la autenticación

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            //Usuario normal y usuario admin
            //ADMIN
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("adminUser@admin.com");
            adminUser.setRole(ROLE.ADMIN);
            adminUser.setPassword(this.passwordEncoder.encode("admin123"));
            userRepository.save(adminUser);

            //USUARIO
            User user = new User();
            user.setUsername("normalUser");
            user.setEmail("normalUser@normal.com");
            user.setPassword(this.passwordEncoder.encode("user123"));
            userRepository.save(user);

        }
        System.out.println("Verificando datos...");
        System.out.println("USUARIOS CARGADOS: " + userRepository.count());
    }
}
