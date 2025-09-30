package moviefyPackge.moviefy.domain.Entities.validations;

import moviefyPackge.moviefy.domain.Entities.validations.impl.ValidStartEndTimesImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidStartEndTimesImpl.class)
public @interface ValidStartEndTimes {
    String message() default "Invalid time format. Please use ISO offset date time format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
