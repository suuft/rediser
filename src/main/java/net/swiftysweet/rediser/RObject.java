package net.swiftysweet.rediser;

import java.util.concurrent.CompletableFuture;

public interface RObject {

    void push();
    default void pushRedis(RObject object) {
        try {
            Rediser.put(object);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    default void pushAsynchronously() {
        CompletableFuture.runAsync(this::push);
    }

}
