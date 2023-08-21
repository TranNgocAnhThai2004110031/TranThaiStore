package com.tranthai.tranthaistore.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tranthai.tranthaistore.dto.UserDTO;
import com.tranthai.tranthaistore.model.Role;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.repository.UserRepository;
import com.tranthai.tranthaistore.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password!!!");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), 
                    user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }   

    @Override
    public User addUser(UserDTO userDTO) {
        // if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
        //     throw new UsernameNotFoundException("Password and confirm password do not match!!!");
        // }
        User user = new User(userDTO.getFirstName(), 
            userDTO.getLastName(), userDTO.getEmail(),
            this.passwordEncoder.encode(userDTO.getPassword()),
            Arrays.asList(new Role("ROLE_USER")));
        return this.userRepository.save(user);
    }

    @Override
    public void removeUserById(long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword().toString()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        // user.setRoles(Arrays.asList(new Role(user.getRoles().toString())));
        this.userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public List<User> searchUser(String keyword) {
        return this.userRepository.findByEmailContainingIgnoreCase(keyword);
    }
    
}
