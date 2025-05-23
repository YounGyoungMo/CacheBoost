package com.example.CacheBoost.domain.address.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.address.dto.RequestDto.AddressRequestDto;
import com.example.CacheBoost.domain.address.dto.ResponseDto.AddressResponseDto;
import com.example.CacheBoost.domain.address.entity.Address;
import com.example.CacheBoost.domain.address.repository.AddressRepository;
import com.example.CacheBoost.domain.user.entity.User;
import com.example.CacheBoost.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressService {

    private final AddressRepository addressRepository;

    private final UserService userService;

    @Transactional
    public AddressResponseDto saveAddress(Long userId, AddressRequestDto requestDto) {

        User user = userService.findByIdOrElseThrow(userId);

        Address address = Address.builder()
                .address(requestDto.getAddress())
                .user(user)
                .build();

        addressRepository.save(address);
        return AddressResponseDto.from(address);
    }

    public List<AddressResponseDto> findAll() {
        //어드레스id가 있는 지 확인 있으면  stream으로 갖고옴
        return addressRepository.findAll()
                .stream()
                .map(AddressResponseDto::from)
                .toList();
    }

    @Transactional
    public AddressResponseDto updateAddress(Long addressId, Long userId, String newAddress) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        // 주소의 소유자가 요청한 사용자와 일치하는지 확인
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        address.updateAddress(newAddress);
        return AddressResponseDto.from(address);
    }

    @Transactional
    public void removeAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        // 주소의 소유자가 요청한 사용자와 일치하는지 확인
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        addressRepository.delete(address);
    }
}