package com.dipadev.personal_blog.services.business;

import com.dipadev.personal_blog.dtos.LoginRequestDTO;
import com.dipadev.personal_blog.dtos.LoginResponseDTO;
import com.dipadev.personal_blog.dtos.UserRequestDTO;
import com.dipadev.personal_blog.dtos.UserResponseDTO;
import com.dipadev.personal_blog.exceptions.BusinessException;
import com.dipadev.personal_blog.mappers.UserMapper;
import com.dipadev.personal_blog.models.User;
import com.dipadev.personal_blog.repository.UserRepository;
import com.dipadev.personal_blog.services.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO postUser(UserRequestDTO userRequest) {
        // 1. Convert DTO to entity
        User user = userMapper.toEntity(userRequest);

        // 2. Business logic (encrypt password)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Business logic (user type 2 the only option)
        user.setTypeUser((byte) 2);

        // 4. Save
        User savedUser = userRepository.save(user);

        // 5. Return DTO response (without password)
        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // 1. Find user by username
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new BusinessException("Invalid username or password"));

        // 2. Validate password
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }

        // 3. Generate JWT token
        String token = jwtService.generateToken(user);

        // 4. Return response with token
        return new LoginResponseDTO(token, user.getId(), user.getUsername());
    }
}
