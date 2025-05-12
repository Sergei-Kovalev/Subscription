package jdev.kovalev.controller;

import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/top")
    public ResponseEntity<List<SubscriptionResponseDto>> getTop() {
        return new ResponseEntity<>(subscriptionService.getTop(), HttpStatus.OK);
    }
}
