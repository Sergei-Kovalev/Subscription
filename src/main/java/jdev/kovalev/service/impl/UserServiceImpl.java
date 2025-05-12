package jdev.kovalev.service.impl;

import jdev.kovalev.mapper.SubscriptionMapper;
import jdev.kovalev.mapper.UserMapper;
import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.dto.response.UserResponseDto;
import jdev.kovalev.entity.Subscription;
import jdev.kovalev.entity.User;
import jdev.kovalev.exception.SubscriptionNotFoundException;
import jdev.kovalev.exception.UserNotFoundException;
import jdev.kovalev.repository.SubscriptionRepository;
import jdev.kovalev.repository.UserRepository;
import jdev.kovalev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final static String SUCCESSFULLY_DELETED = "Пользователь с id: %s успешно удалён";
    private final static String SUBSCRIPTION_ADDED = "Пользователю %s %s успешно добавлена подписка \"%s\"";
    private final static String SUBSCRIPTION_REMOVED = "Пользователь %s %s успешно удалил подписку \"%s\"";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public UserResponseDto findById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(userMapper::toUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {
        return userMapper.toUserResponseDto(userRepository.save(userMapper.toUser(userRequestDto)));
    }

    @Transactional
    @Override
    public UserResponseDto update(String userId, UserRequestDto userRequestDto) {
        User userForUpdate = findUserById(userId);
        userMapper.updateUser(userForUpdate, userRequestDto);
        User updatedUser = userRepository.save(userForUpdate);
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public String delete(String userId) {
        userRepository.deleteById(UUID.fromString(userId));
        return String.format(SUCCESSFULLY_DELETED, userId);
    }

    @Transactional
    @Override
    public String addSubscription(String userId, String subscriptionId) {
        User user = findUserById(userId);
        Subscription subscription = findSubscriptionById(subscriptionId);

        user.addSubscription(subscription);

        return String.format(SUBSCRIPTION_ADDED, user.getFirstName(), user.getLastName(), subscription.getDigitalServiceName());
    }

    @Override
    public List<SubscriptionResponseDto> getUserSubscriptions(String userId) {
        User user = findUserById(userId);
        return user.getSubscriptions().stream()
                .map(subscriptionMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public String removeSubscriptionFromUser(String userId, String subscriptionId) {
        User user = findUserById(userId);
        Subscription subscription = findSubscriptionById(subscriptionId);

        user.removeSubscription(subscription);

        return String.format(SUBSCRIPTION_REMOVED, user.getFirstName(), user.getLastName(), subscription.getDigitalServiceName());
    }

    private User findUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }


    private Subscription findSubscriptionById(String subscriptionId) {
        return subscriptionRepository.findById(UUID.fromString(subscriptionId))
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));
    }
}
