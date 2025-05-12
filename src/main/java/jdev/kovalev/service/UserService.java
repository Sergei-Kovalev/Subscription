package jdev.kovalev.service;

import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto findById(String userId);
    UserResponseDto create(UserRequestDto userRequestDto);
    UserResponseDto update(String userId, UserRequestDto userRequestDto);
    String delete(String userId);

    String addSubscription(String userId, String subscriptionId);
    List<SubscriptionResponseDto> getUserSubscriptions(String userId);
    String removeSubscriptionFromUser(String userId, String subscriptionId);
}
