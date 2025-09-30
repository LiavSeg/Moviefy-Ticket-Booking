package moviefyPackge.moviefy.domain.Entities.validations.impl;

import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.Entities.validations.ValidStartEndTimes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class ValidStartEndTimesImpl implements ConstraintValidator<ValidStartEndTimes, ShowtimeEntity> {

    private String message;

    @Override
    public void initialize(ValidStartEndTimes constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(ShowtimeEntity value, ConstraintValidatorContext context) {
        if (value == null) return true;

        LocalDateTime start = value.getStartTime();
        LocalDateTime end   = value.getEndTime();

        if (start == null || end == null) return true;

        if (start.isBefore(end)) return true;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("endTime") // ממקם את ההפרה על endTime
                .addConstraintViolation();
        return false;
    }
}
