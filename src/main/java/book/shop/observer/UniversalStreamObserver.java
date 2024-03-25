package book.shop.observer;

import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;

// Logging for example
@Log4j2(topic = "observer")
public class UniversalStreamObserver<T> implements StreamObserver<T> {
    @Override
    public void onNext(T t) {
        log.info("Got result: " + t.toString());
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("An error: " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("Method complete");
    }
}
