## Rediser

---
WARN : BAD CODE! NEED REFACTORING!
Redis is able to process requests as quickly as possible, save information when disconnected, and much more. This framework is intended for those people who use redis as the main data storage.

### `Project To-Do:`
* Add support for other storages.
* Rename the framework. (Come up with a good name)
* Add the possibility to create local fields.
* Make instructions for the correct use of Redis.
---
### `Usage examples:`
First, let's create a class whose objects we will store in Redis. The class must implementation from `net.rediser.RObject`, have an annotation `@Redis` and a field with an annotation `@KeyField`. Example of correct use:
```java
@Getter // not necessary
@Setter // not necessary
@AllArgsConstructor // not necessary
@NoArgsConstructor // not necessary
@Redis(name = "test_redis", port = 6379) // need. from here we get the redis credinals, where we store the data
public class User implements RObject {

    @KeyField // indicates the field that will be the key in the redis.
    long identifier;

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
```
### `How to work with this class?)`
It's simple! When running your code, you first need to register your class:
```java
Rediser.registerClass(User.class);
```
Now we can receive and send users to Redis. We get the objects through the `net.rediser.Rediser` class. Please remember that if there is no object in redis, redis will return null. Example:
```java
        // get user by specifying class whose object need
        // and the key that we use in the class
        // (an object of the field type over which the annotation @KeyField).
        User user = Rediser.get(User.class, 1);

        // checking, because if the object is not in redis, null will be returned to us.
        if (user == null) {
            // if user not found, we create an instance of it and send it to redis.
            user = new User(1, "Juhani", "Laine", 17);
            user.push();
        }

        // for example, change some field.
        user.setAge(5);

        // and save
        user.push();
```
---

If you have any ideas, or you have found problems, write to me or make a pull requests.