package com.iss.patient_medicine.service.impl;

import com.alibaba.dashscope.aigc.generation.*;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.*;
import com.iss.patient_medicine.service.AiSymptomCheckerService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QwenAiSymptomCheckerServiceImpl implements AiSymptomCheckerService {

    private static final List<String> FALLBACK = List.of("全科医学科", "内科", "外科");

    @Override
    public List<String> checkSymptoms(String symptoms) {
        try {
            Generation gen = new Generation();
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content("你是一名分诊助手。请根据患者描述的症状，返回 2~5 个最可能的科室，用英文逗号分隔，不要多余解释。示例：呼吸内科,消化内科,神经内科")
                    .build();
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(symptoms)
                    .build();
            GenerationParam param = GenerationParam.builder()
                    .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                    .model("qwen-plus")
                    .messages(List.of(systemMsg, userMsg))
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            GenerationResult result = gen.call(param);
            String resp = result.getOutput().getChoices().get(0).getMessage().getContent();

            // 打印原始响应，方便排查
            System.out.println("DashScope raw response: " + resp);

            // 兼容中文逗号、换行、空格
            List<String> list = Arrays.stream(resp.split("[,，\\s]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            return list.isEmpty() ? FALLBACK : list;

        } catch (Exception e) {
            e.printStackTrace();
            return FALLBACK;
        }
    }
}