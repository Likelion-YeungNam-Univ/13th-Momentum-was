package Momentum.heatcaution.controller;

import Momentum.heatcaution.exception.LoginRequiredException;
import Momentum.heatcaution.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Alert API", description = "위험 상태 시 보호자에게 알림 전송 API")
@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    private String getUsernameFromSession(HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            throw new LoginRequiredException();
        }
        return username;
    }

    @Operation(summary = "위험 상태 알림 트리거", description = "현재 로그인한 사용자의 보호자에게 위험 상태를 알립니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 처리 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @PostMapping("/risk")
    public ResponseEntity<Map<String, Object>> alertRisk(
            @RequestParam @NotBlank String level,
            @RequestParam(required = false) Double prob,
            HttpSession session
    ) {
        String username = getUsernameFromSession(session);
        alertService.alertGuardians(username, level, prob);

        Map<String, Object> body = new HashMap<>();
        body.put("sent", true);
        return ResponseEntity.ok(body);
    }
}