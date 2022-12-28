package net.rediser.test;


import net.rediser.Rediser;

public class Test {

    public static void main(String[] args) {
        // registering class in redisser
        Rediser.registerClass(User.class);

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
    }

//    private static void test() {
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            System.out.println("put to redis user #" + i);
//            new User(i, "user", "" + i, i).push();
//        }
//        for (int i = 0; i < 100; i++) {
//            System.out.println("get from redis user #" + i);
//            System.out.println("#" + Rediser.get(User.class, i).getLastName() + " user has been loaded");
//
//        }
//        System.out.println("Execution time : " + (System.currentTimeMillis() - startTime) + "ms");
//    }

}
