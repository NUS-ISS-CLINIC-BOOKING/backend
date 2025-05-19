package com.iss.auth.application;

import com.iss.auth.dto.ModifyHealthInfoCommand;
import com.iss.auth.dto.ModifyHealthInfoResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    public ModifyHealthInfoResult modifyHealthInfo(Long UserID, @Valid ModifyHealthInfoCommand request) {
        return null;
    }
}
