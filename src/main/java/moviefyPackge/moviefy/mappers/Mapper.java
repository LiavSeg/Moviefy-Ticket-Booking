package moviefyPackge.moviefy.mappers;

/**
 * Generic interface for bidirectional mapping between two types.
 * @param <A> source type
 * @param <B> target type
 */
public interface Mapper<A,B>{
    B mapTo(A a);
    A mapFrom(B b);
}
