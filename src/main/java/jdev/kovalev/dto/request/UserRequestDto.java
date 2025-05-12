package jdev.kovalev.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Length(max = 30, message = "Максимальная длина имени 30 символов")
    private String firstName;

    @NotBlank(message = "Фамилия пользователя не может быть пустым")
    @Length(max = 30, message = "Максимальная длина фамилии 30 символов")
    private String lastName;
}
