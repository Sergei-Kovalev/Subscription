package jdev.kovalev.service;

import jdev.kovalev.dto.response.SubscriptionResponseDto;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionResponseDto> getTop();
}
