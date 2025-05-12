package jdev.kovalev.controller;

import jakarta.validation.Valid;
import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.dto.response.UserResponseDto;
import jdev.kovalev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@UUID @PathVariable String id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.create(userRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@UUID @PathVariable String id,
                                                  @Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.update(id, userRequestDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> delete(@UUID @PathVariable String id) {
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/subscriptions", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> addSubscription(@UUID @PathVariable String id,
                                                  @UUID @RequestParam String subId) {
        return new ResponseEntity<>(userService.addSubscription(id, subId), HttpStatus.OK);
    }

    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<List<SubscriptionResponseDto>> getUserSubscriptions(@UUID @PathVariable String id) {
        return new ResponseEntity<>(userService.getUserSubscriptions(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/subscriptions/{sub_id}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> removeSubscriptionFromUser(@UUID @PathVariable String id,
                                                             @UUID @PathVariable String sub_id) {
        return new ResponseEntity<>(userService.removeSubscriptionFromUser(id, sub_id), HttpStatus.OK);
    }
}
