package br.com.phsaraiva.Whatsapp_clone_api.model.user.repository;

import br.com.phsaraiva.Whatsapp_clone_api.model.user.User;
import br.com.phsaraiva.Whatsapp_clone_api.model.user.constants.UserConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, String> {

    @Query(name = UserConstants.FIND_USER_BY_EMAIL)
    Optional<User> findByEmail(@Param("email") String userEmail);
@Query(name = UserConstants.FIND_USER_BY_PUBLIC_ID)
    Optional<User> findUserByPublicId(@Param("publicId") String publicId);
    @Query(UserConstants.FIND_ALL_USERS_EXCEPT_SELF)
    List<User> findAllUsersExceptSelf(@Param("publicID") String senderID);
}
