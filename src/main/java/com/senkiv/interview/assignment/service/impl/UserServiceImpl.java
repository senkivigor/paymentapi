package com.senkiv.interview.assignment.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.senkiv.interview.assignment.domain.dto.VerifyUserRequestDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserResponseDTO;
import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.repository.UserRepository;
import com.senkiv.interview.assignment.service.UserService;
import com.senkiv.interview.assignment.service.validation.ErrorCode;

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
		return user == null ? buildFailVerifyUserResponse(userRequestDTO) : buildSuccessVerifyUserResponse(user);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	@Override
	public void changeUserBalance(User user, BigDecimal userBalanceChange) {
		user.setBalance(user.getBalance().add(userBalanceChange));
	}

	private VerifyUserResponseDTO buildFailVerifyUserResponse(VerifyUserRequestDTO verifyUserRequestDTO) {
		return VerifyUserResponseDTO.verifyUserResponseDTO()
				.withUserId(verifyUserRequestDTO.getUserId())
				.withSuccess(false)
				.withErrCode(ErrorCode.USER_NOT_FOUND.getCode())
				.withErrMsg("Unknown userId").build();
	}

	private VerifyUserResponseDTO buildSuccessVerifyUserResponse(User user) {
		return VerifyUserResponseDTO.verifyUserResponseDTO().withUserId(user.getUserId())
				.withSuccess(true)
				.withUserCat(user.getUserCat())
				.withKycStatus(user.getKycStatus())
				.withSex(user.getSex())
				.withFirstName(user.getFirstName())
				.withLastName(user.getLastName())
				.withStreet(user.getStreet())
				.withCity(user.getCity())
				.withZip(user.getZip())
				.withCountry(user.getCountry())
				.withEmail(user.getEmail())
				.withDob(user.getDob())
				.withMobile(user.getMobile())
				.withBalance(user.getBalance())
				.withBalanceCy(user.getBalanceCy())
				.withLocale(user.getLocale())
				.build();
	}
}
