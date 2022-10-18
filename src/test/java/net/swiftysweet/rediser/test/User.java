package net.swiftysweet.rediser.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swiftysweet.rediser.RObject;
import net.swiftysweet.rediser.annotation.KeyField;
import net.swiftysweet.rediser.annotation.Redis;

@Getter // not necessary
@Setter // not necessary
@AllArgsConstructor // not necessary
@NoArgsConstructor // not necessary
@Redis(name = "test_redis", port = 6379) // need. from here we get the redis credinals, where we store the data
public class User implements RObject {

    // @KeyField indicates the field that will be the key in the redis.
    @KeyField long identifier;

    String name;
    String lastName;
    int age;

    /**
     * One of the necessities, in the future when I add to the lib
     * database support is not kv, then it will come in handy, but now there is nothing
     * it is difficult to redefine one method. The method pushAsynchronously() - executes
     * it's the same, only asynchronously.
     */
    @Override
    public void push() {
        pushRedis(this);
    }
}
