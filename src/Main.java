import java.util.Random;

import rx.Observable;
import rx.Subscriber;

/**
 * @author vincent
 */
public class Main {
    private static final int MAX_SIZE = 5;
    private static final int MAX_VALUE = 8;
    private static final int MAX_RANDOM_VALUE = 10;

    public static void main(String[] args) {
        createObservable();
    }

    private static void createObservable() {
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                int temp;
                for (int i = 0; i < MAX_SIZE; i++) {
                    temp = new Random().nextInt(MAX_RANDOM_VALUE);
                    if (temp > MAX_VALUE) {
                        subscriber.onError(new Throwable(String.format("value=%d,is >%d", temp, MAX_VALUE)));
                        break;
                    } else {
                        subscriber.onNext(temp);
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("结束");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });
    }
}
