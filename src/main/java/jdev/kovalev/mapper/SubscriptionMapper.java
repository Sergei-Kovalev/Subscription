package jdev.kovalev.mapper;

import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionResponseDto toDto(Subscription subscription);
}
