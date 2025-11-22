package com._oormthonUNIV.newnew.security.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpRequest {

    @NotBlank(message = "아이디는 비어있을 수 없습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;

    @NotBlank(message = "별명은 비어있을 수 없습니다.")
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private LocalDate birth;

}
