package Momentum.heatcaution.controller;

import Momentum.heatcaution.exception.LoginRequiredException;
import Momentum.heatcaution.service.InviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Invite API", description = "보호자 초대 및 카카오 동의 플로우")
@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    private String getUsernameFromSession(HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            throw new LoginRequiredException();
        }
        return username;
    }

    @Operation(summary = "초대 토큰 발급")
    @PostMapping
    public ResponseEntity<InviteService.CreateInviteResponse> createInvite(
            @RequestParam String name,
            @RequestParam String phone,
            HttpSession session
    ) {
        String username = getUsernameFromSession(session);
        return ResponseEntity.ok(inviteService.createInvite(username, name, phone));
    }

    @Operation(summary = "카카오 인증 URL 받기")
    @GetMapping("/kakao/authorize")
    public ResponseEntity<Map<String, String>> kakaoAuthorize(@RequestParam String token) {
        String url = inviteService.buildKakaoAuthorizeUrl(token);
        return ResponseEntity.ok(Map.of("authorizeUrl", url));
    }

    @Operation(summary = "카카오 콜백 수신")
    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(@RequestParam String code, @RequestParam String state) {
        inviteService.handleKakaoCallback(code, state);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "(디버그) 친구 목록 확인")
    @GetMapping("/kakao/friends")
    public ResponseEntity<InviteService.FriendsResponse> friends(@RequestParam String token) {
        return ResponseEntity.ok(inviteService.getFriends(token));
    }

    @Operation(summary = "(디버그) 메시지 발송")
    @PostMapping("/kakao/send")
    public ResponseEntity<InviteService.SendResponse> send(@RequestParam String token, @RequestParam String text) {
        return ResponseEntity.ok(inviteService.sendMessageToAll(token, text));
    }
}


