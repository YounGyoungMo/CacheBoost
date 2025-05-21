package com.example.CacheBoost.domain.address.dto.ResponseDto;

import com.example.CacheBoost.domain.address.entity.Address;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressResponseDto {

    private Long userId;
    private Long id;
    private String address;

    public static AddressResponseDto from(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .userId(address.getUser().getId())
                .address(address.getAddress())
                .build();
    }
}
