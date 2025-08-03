package com.iss.symptom_checker.controller;

import com.iss.symptom_checker.application.SymptomCheckerApplicationService;
import com.iss.symptom_checker.dto.SymptomCheckCommand;
import com.iss.symptom_checker.dto.SymptomCheckResult;
import com.iss.common.common.response.SuccessResponse; // 注意：这里引用了 common 模块
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/symptom-check")
@CrossOrigin(origins = "*") // 允许跨域，生产环境应更精确配置
public class SymptomCheckerController {

    private final SymptomCheckerApplicationService symptomCheckerApplicationService;

    // 显式定义构造函数
    public SymptomCheckerController(SymptomCheckerApplicationService symptomCheckerApplicationService) {
        this.symptomCheckerApplicationService = symptomCheckerApplicationService;
    }

    @PostMapping("/check")
    public SuccessResponse<SymptomCheckResult> checkSymptoms(@Valid @RequestBody SymptomCheckCommand command) {
        SymptomCheckResult result = symptomCheckerApplicationService.checkSymptoms(command);
        return new SuccessResponse<>(result);
    }
}