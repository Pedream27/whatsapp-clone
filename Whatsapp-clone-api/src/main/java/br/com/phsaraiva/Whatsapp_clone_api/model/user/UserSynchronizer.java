package br.com.phsaraiva.Whatsapp_clone_api.model.user;

import br.com.phsaraiva.Whatsapp_clone_api.model.user.mapper.UserMapper;
import br.com.phsaraiva.Whatsapp_clone_api.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void synchronizedWithIdp(Jwt token) {
        log.info("Synchronizing user with idp");
        getUserEmail(token).ifPresent(userEmail -> {
            log.info("Synchronizing user having email {}", userEmail);
            //Optional<User> optionalUser = userRepository.findByEmail(userEmail);
            User user = userMapper.fromTokenAttributes(token.getClaims());
            //optionalUser.ifPresent(value -> user.setId(optionalUser.get().getId()));
            userRepository.save(user);
        });

    }

    private Optional<String> getUserEmail(Jwt token){
        Map<String, Object> atributes  = token.getClaims();
        if (atributes.containsKey("email")) {
            return Optional.of(atributes.get("email").toString());
        }
    return Optional.empty();
    }
}
