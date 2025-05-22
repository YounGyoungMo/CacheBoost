package com.example.CacheBoost.domain.address.controller;

import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.address.dto.RequestDto.AddressRequestDto;
import com.example.CacheBoost.domain.address.dto.ResponseDto.AddressResponseDto;
import com.example.CacheBoost.domain.address.service.AddressService;
import com.example.CacheBoost.domain.auth.AuthUser;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/address")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressController {

    final private AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> saveAddress(
            @Valid  @AuthUser Long userId, @RequestBody AddressRequestDto requestDto
    ) {
        AddressResponseDto responseDto = addressService.saveAddress(userId, requestDto);
        return ResponseEntity
                .status(SuccessCode.ADD_ADDRESS_SUCCESS.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.ADD_ADDRESS_SUCCESS, responseDto));
    }

    @GetMapping
    public ResponseEntity
            <ApiResponseDto<List<AddressResponseDto>>> findAll() {
        List<AddressResponseDto> responseDto = addressService.findAll();
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.SEARCH_ADDRESS_SUCCESS, responseDto));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> updateAddress( @Valid @AuthUser Long userId, @PathVariable Long addressId, @RequestBody AddressRequestDto requestDto) {
        AddressResponseDto responseDto = addressService.updateAddress(addressId, userId, requestDto.getAddress());
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.UPDATE_ADDRESS_SUCCESS, responseDto));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponseDto<Void>> removeAddress( @Valid @AuthUser Long userId, @PathVariable Long addressId) {
        addressService.removeAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DELETE_ADDRESS_SUCCESS));
    }

}
