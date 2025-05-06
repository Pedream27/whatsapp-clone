package br.com.phsaraiva.Whatsapp_clone_api.domain.user.service;

import br.com.phsaraiva.Whatsapp_clone_api.domain.user.UserResponse;
import br.com.phsaraiva.Whatsapp_clone_api.domain.user.mapper.UserMapper;
import br.com.phsaraiva.Whatsapp_clone_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsersExceptSelf(Authentication connctUser){

        return userRepository.findAllUsersExceptSelf(connctUser.getName())
                .stream()
                .map(userMapper:: toUserResponse)
                .toList();
    }


}
