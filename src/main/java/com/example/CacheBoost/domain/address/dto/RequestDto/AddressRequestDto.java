package com.example.CacheBoost.domain.address.dto.RequestDto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class AddressRequestDto {

    //private Long userId;

    @Column(nullable = false)
    private String address;

}
