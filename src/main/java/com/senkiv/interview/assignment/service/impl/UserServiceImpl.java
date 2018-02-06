package com.senkiv.interview.assignment.service.impl;

import com.senkiv.interview.assignment.domain.dto.VerifyUserRequestDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserResponseDTO;
import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.domain.entity.converter.EntityConverter;
import com.senkiv.interview.assignment.repository.UserRepository;
import com.senkiv.interview.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public VerifyUserResponseDTO verifyUser(VerifyUserRequestDTO userRequestDTO) {
        User user = userRepository.findOne(userRequestDTO.getUserId());
        return user == null ? EntityConverter.USER_NOT_FOUND_DTO.apply(userRequestDTO.getUserId()) :
                EntityConverter.USER_TO_DTO.apply(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void changeUserBalance(User user, BigDecimal userBalanceChange) {
        user.setBalance(user.getBalance().add(userBalanceChange));
    }
}
