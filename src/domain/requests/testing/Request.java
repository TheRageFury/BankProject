package domain.requests.testing;

public interface Request {
    boolean doesItMatch(Object toTest, RequestMode mode);
}