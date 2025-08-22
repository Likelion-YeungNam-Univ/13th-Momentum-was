package Momentum.heatcaution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "회원가입 요청 DTO")
public record RegisterRequest(
        @Schema(description = "사용자 아이디", example = "newuser")
        String username,

        @Schema(description = "비밀번호", example = "password1234")
        String password,

        @Schema(description = "사용자 이름", example = "홍길동")
        String name,

        @Schema(description = "전화번호", example = "010-1234-5678")
        @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "전화번호 형식은 010-1234-5678입니다")
        String phone,

        @Schema(description = "생년월일 (yyyy-MM-dd)", example = "2000-01-01")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식은 yyyy-MM-dd입니다")
        String birth
) {}
