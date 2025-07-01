package fpt.backend.MasterAuth.user.impl;

import fpt.backend.MasterAuth.exception.EmailAlreadyExisted;
import fpt.backend.MasterAuth.requests.AddUserRequest;
import fpt.backend.MasterAuth.requests.UpdateUserRequest;
import fpt.backend.MasterAuth.user.Role;
import fpt.backend.MasterAuth.user.User;
import fpt.backend.MasterAuth.user.UserRepository;
import fpt.backend.MasterAuth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->new BadCredentialsException("Username or password is wrong!"));
        return (User) authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
               email,password
            )
        ).getPrincipal();

    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    @CachePut(value = "users",key = "#request.email")
    public User createUser(AddUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExisted("Email has already existed!");
        }
        return userRepository.save(
            User.builder()
                .email(request.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .roles(List.of(Role.USER))
                .password(passwordEncoder.encode(request.getPassword()))
                .build()
        );

    }

    @Override
    public void addRole(Long userId, Role role) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(request.getFirstname()).ifPresent(user::setFirstname);
        Optional.ofNullable(request.getLastname()).ifPresent(user::setLastname);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
