package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.tranthai.tranthaistore.dto.UserDTO;
import com.tranthai.tranthaistore.model.User;

public interface UserService extends UserDetailsService{
    List<User> getAllUser();

    User addUser(UserDTO userDTO);

    void removeUserById(long id);

    public Optional<User> getUserById(long id);

    void updateUser(User user);

    User getUserByEmail(String email);

    List<User> searchUser(String keyword);
}
