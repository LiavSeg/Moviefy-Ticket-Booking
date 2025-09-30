package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.XmlEntity;
import org.springframework.data.repository.CrudRepository;
/**
 * Repository interface for managing XML report entity persistence.
 * Provides standard CRUD operations for XmlEntity objects, which track
 * import/export actions and their metadata in the system.
 */
public interface XmlReportRepository extends CrudRepository<XmlEntity,Long> {
}
