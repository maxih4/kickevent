package com.example.kickevent.services;

import com.example.kickevent.exceptions.UsernameAlreadyTakenException;
import com.example.kickevent.model.Role;
import com.example.kickevent.model.User;
import com.example.kickevent.repositories.RefreshTokenRepository;
import com.example.kickevent.repositories.RoleRepository;
import com.example.kickevent.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, RefreshTokenRepository refreshTokenRepository) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public User createPerson(String userName, String password) {
        if (userRepository.findByUserName(userName).isPresent()) throw new UsernameAlreadyTakenException(userName);
        User user = User.builder().userName(userName).password(password).roles(Collections.singletonList(roleRepository.findByName("USER").get())).build();
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return createPerson(user.getUserName(), bcryptEncoder.encode(user.getPassword()));

    }

    public User update(User user){
        if (userRepository.findByUserName(user.getUserName()).isPresent() && !Objects.equals(userRepository.findByUserName(user.getUserName()).get().getId(), user.getId())) throw new UsernameAlreadyTakenException(user.getUserName());
        return userRepository.save(user);
    }

    @Transactional
    public User delete(User user) {
        user.setRoles(null);

        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }


    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
