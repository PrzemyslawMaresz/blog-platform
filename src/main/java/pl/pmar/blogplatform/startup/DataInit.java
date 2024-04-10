package pl.pmar.blogplatform.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pmar.blogplatform.model.entity.Role;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.model.enums.RoleEnum;
import pl.pmar.blogplatform.model.payload.request.SignupRequest;
import pl.pmar.blogplatform.repository.RoleRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import pl.pmar.blogplatform.service.AuthService;

import java.util.Set;

@Component
public class DataInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final AuthService authService;

    private final UserRepository userRepository;

    @Value("${blog-platform.admin.username}")
    private String adminUsername;

    @Value("${blog-platform.admin.password}")
    private String adminPassword;


    @Autowired
    public DataInit(
            RoleRepository roleRepository,
            AuthService authService,
            UserRepository userRepository
    ) {
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        initRoles();
        initAdmin();
    }

    private void initRoles() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(new Role(1 ,RoleEnum.ROLE_USER));
            roleRepository.save(new Role(2, RoleEnum.ROLE_MODERATOR));
            roleRepository.save(new Role(3, RoleEnum.ROLE_ADMIN));
        }
    }

    private void initAdmin() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setUsername(adminUsername);
            signupRequest.setEmail("admin@gmail.com");
            signupRequest.setPassword(adminPassword);
            authService.registerUser(signupRequest);

            User admin = userRepository.findByUsername(adminUsername).get();
            Set<Role> roles = Set.of(roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElseThrow());
            admin.setRoles(roles);
            userRepository.save(admin);
        }
    }
}
