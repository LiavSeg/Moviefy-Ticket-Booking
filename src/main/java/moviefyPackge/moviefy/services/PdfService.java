package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import org.springframework.stereotype.Component;

@Component
public interface PdfService {
    byte[] generateTicketPdf(BookingEntity booking) throws Exception;

}