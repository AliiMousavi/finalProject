package com.example.phase2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDtoWithoutComment {
    @NotNull
    @Min(value = 1, message = "score must be between 1 to 5")
    @Max(value = 5, message = "score must be between 1 to 5")
    private int score;
}
