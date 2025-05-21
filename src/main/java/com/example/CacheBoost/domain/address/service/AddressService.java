package com.example.CacheBoost.domain.address.service;

import com.example.CacheBoost.domain.address.dto.RequestDto.AddressRequestDto;
import com.example.CacheBoost.domain.address.dto.ResponseDto.AddressResponseDto;

import java.util.List;

public interface AddressService {
    //CreateAddressResponseDto saveAdress(String address);
    List<AddressResponseDto> findAll();
    void removeAddress(Long userId, Long addressId);
    AddressResponseDto saveAddress(Long userId, AddressRequestDto requestDto);
    AddressResponseDto updateAddress(Long addressId, Long userId, String newAddress);
}
