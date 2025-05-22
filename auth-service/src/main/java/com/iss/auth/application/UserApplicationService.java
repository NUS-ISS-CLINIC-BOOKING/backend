package com.iss.auth.application;

import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.repository.UserRepository;
import com.iss.auth.dto.ModifyHealthInfoCommand;
import com.iss.auth.dto.ModifyHealthInfoResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;

    public ModifyHealthInfoResult modifyHealthInfo(long UserID, @Valid ModifyHealthInfoCommand request) {
        userRepository.ModifyUserHealthInfo(UserID, request.getAllergyInfo());
        return null;
    }
}
