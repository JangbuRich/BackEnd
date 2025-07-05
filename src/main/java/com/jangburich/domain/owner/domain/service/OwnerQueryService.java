package com.jangburich.domain.owner.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.owner.domain.service.dtos.BusinessNoDto;
import com.jangburich.utils.RestClientUtil;
import com.jangburich.domain.owner.domain.controller.dto.res.OwnerGetResDTO;
import com.jangburich.domain.owner.domain.controller.dto.res.OwnerResDto;
import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OwnerQueryService {
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final RestClientUtil restClientUtil;

    @Value("${jangburich.public.data.key}")
    private String serviceKey;

    public OwnerGetResDTO getOwnerInfo(String customOAuthUser) {
        User user = userRepository.findByProviderId(customOAuthUser)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
        return OwnerGetResDTO.of(owner);
    }

    /**
     * 사업자 번호가 유효한지 조회한다.
     * @param businessNo - target 사업자 번호
     * @return OwnerValidationResDto
     */
    public OwnerResDto.OwnerValidationResDto getValidateBusinessNo (String businessNo) {
        BusinessNoDto.BusinessNoResponseDto responseDto = restClientUtil.callPostRestClient(
            "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + serviceKey,
            BusinessNoDto.BusinessNoRequestDto.of(businessNo),
            BusinessNoDto.BusinessNoResponseDto.class
        );

        boolean activeBusiness = responseDto.getBusinessInfoList().getFirst().isActiveBusiness();

        if(activeBusiness)
            return OwnerResDto.OwnerValidationResDto.of(true);
        else
            return OwnerResDto.OwnerValidationResDto.of(false);
    }

    /**
     * 계좌주 & 계좌번호가 유효한지 조회한다.
     * @param account - 은행명
     * @param accountNo - 계좌번호
     * @return OwnerValidationResDto
     */
    public OwnerResDto.OwnerValidationResDto getValidateAccountNo (String account, String accountNo) {

        return null;
    }

}
