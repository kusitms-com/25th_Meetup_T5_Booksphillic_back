package com.booksphillic.controller;

import com.booksphillic.domain.user.InquiryType;
import com.booksphillic.response.BaseException;
import com.booksphillic.response.BaseResponse;
import com.booksphillic.service.auth.AuthService;
import com.booksphillic.service.auth.JwtService;
import com.booksphillic.service.user.OwnerService;
import com.booksphillic.service.user.dto.GetOwnerInquiryRes;
import com.booksphillic.service.user.dto.GetOwnerProfileRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;
    private final JwtService jwtService;
    private final AuthService authService;

    /**
     * 사장님 마이페이지
     */
    // 내 책방 관리
    @GetMapping("/profile")
    public BaseResponse<GetOwnerProfileRes> getStoreProfile(@RequestParam(name = "userId") Long userId) {
        try {
            GetOwnerProfileRes result = ownerService.getStoreProfile(userId);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getCode());
        }
    }

    /***
     * 문의 내역 조회
     */
    @GetMapping("/{ownerId}/inquiry")
    public BaseResponse<List<GetOwnerInquiryRes>> getInquiries(@RequestHeader(required = false, name = "authorization") String authorization,
                                                               @PathVariable Long ownerId,
                                                               @RequestParam(name = "type") InquiryType type) {

        try {
            String jwt = authService.validateBearerToken(authorization);
            jwtService.validateJwt(jwt, ownerId);
            List<GetOwnerInquiryRes> getOwnerInquiries = ownerService.getInquiries(ownerId, type);
            return new BaseResponse<>(getOwnerInquiries);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getCode());
        }
    }
}
