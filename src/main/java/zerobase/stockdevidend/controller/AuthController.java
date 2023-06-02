package zerobase.stockdevidend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.stockdevidend.model.Auth;
import zerobase.stockdevidend.model.MemberEntity;
import zerobase.stockdevidend.security.TokenProvider;
import zerobase.stockdevidend.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
        MemberEntity register = memberService.register(request);

        return ResponseEntity.ok(register);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){
        MemberEntity authenticate = memberService.authenticate(request);
        String token = tokenProvider.generateToken(authenticate.getUsername(), authenticate.getRoles());

        return ResponseEntity.ok(token);
    }
}
