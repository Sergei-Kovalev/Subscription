package jdev.kovalev.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Slf4j(topic = "SocksService")
@RequiredArgsConstructor
@Component
public class LoggingAspect {

    @Before(value = "execution(public * jdev.kovalev.controller.*Controller.*(..))")
    public void loggingControllersBeforeAdvice(JoinPoint joinPoint) {
        String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        log.info("Контроллер: {} Вызван метод {} с полным путем {}", controllerName, methodName, path);
    }

    @After(value = "execution(public * jdev.kovalev.controller.*Controller.*(..))")
    public void loggingControllersAfterAdvice(JoinPoint joinPoint) {
        String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        int status = response != null ? response.getStatus() : 0;
        log.info("Контроллер: {} Вызван метод {} вернул статус {}", controllerName, methodName, status);
    }

    @After(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler) " +
            "&& execution(public * jdev.kovalev.exception.handler.*Handler*.*(..))")
    public void loggingControllerAdvice(JoinPoint joinPoint) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        sendExceptionLog(controllerName, methodName, args);
    }

    @Before(value = "execution(public * jdev.kovalev.service.*.*(..))")
    public void loggingServicesAdvice(JoinPoint joinPoint) {
        String serviceName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Object[] arguments = joinPoint.getArgs();
        log.info("Сервис: {} Вызван метод {} с аргументами {}", serviceName, methodName, Arrays.toString(arguments));
    }

    @AfterThrowing(value = "execution(* jdev.kovalev.service.*.*.*(..))", throwing = "exception")
    public void loggingServicesAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        String serviceName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String exceptionName = exception.getClass().getName();
        String exceptionMessage = exception.getMessage();
        log.warn("Исключение в сервисе {} в методе {}, класс: {}. Сообщение: {}",
                 serviceName, methodName, exceptionName, exceptionMessage);
    }

    private void sendExceptionLog(String controllerName, String methodName, Object[] args) {
        StringBuilder exception = new StringBuilder();
        StringBuilder message = new StringBuilder();

        Optional<Throwable> exceptionsOptional = Arrays.stream(args)
                .filter(arg -> arg instanceof Throwable)
                .map(arg -> (Throwable) arg)
                .findFirst();
        if (exceptionsOptional.isPresent()) {
            exception.append(exceptionsOptional.get().getClass().getName());
            message.append(exceptionsOptional.get().getMessage());
        }

        log.warn("Перехвачено исключение {}, сообщение: {}, контроллер: {}, метод: {}",
                 exception, message, controllerName, methodName);
    }
}
