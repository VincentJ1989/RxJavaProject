import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 过滤操作符
 * @author vincent
 */
public class FilterOperationCharacter {
    public static void main(String[] args) {
        // doDebounce();
        // doDistinct();
        // doElementAt();
        // doFilter();
        // doFirst();
        // doLast();
        // doBlockingObservable();
        // doSkip();
        // doTake();
        // doSkipLast();
        // doTakeLast();
        // doSample();
        doThrottleFirst();
    }

    /**
     * 与sample相反，获取的是规定时间内的第一个数据--android中的防止重复点击可以用
     */
    private static void doThrottleFirst() {
        Observable.interval(500L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .throttleFirst(2000L, TimeUnit.MILLISECONDS)
            .subscribe(System.out::println);
    }

    /**
     * 制定一段时长，时长结束的时候发送最新的数据其余的被过滤
     */
    private static void doSample() {
        Observable.interval(500L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .sample(2000L, TimeUnit.MILLISECONDS)
            .subscribe(System.out::println);
    }

    /**
     * 只取最后的N个数据
     */
    private static void doTakeLast() {
        Observable.just(1, 2, 3, 4, 5, 6).takeLast(2).subscribe(System.out::println);
    }

    /**
     * 过滤最后的N个
     */
    private static void doSkipLast() {
        Observable.just(1, 2, 3, 4, 5, 6).skipLast(2).subscribe(System.out::println);
    }

    /**
     * 只取前N个数据
     */
    private static void doTake() {
        Observable.just(1, 2, 3, 4, 5).take(3).subscribe(System.out::println);
    }

    /**
     * 可以过滤掉前N个数据
     */
    private static void doSkip() {
        Observable.just(1, 2, 3, 4, 5, 6, 7).skip(2).subscribe(System.out::println);
    }

    /**
     * 不会对Observable做处理，只会阻塞，同时它返回的是对应的数据而不是observable
     */
    private static void doBlockingObservable() {
        Integer first = Observable.just(1, 2, 3, 4, 5).toBlocking().first(i -> i > 2);
        System.out.println(first);
    }

    /**
     * 只会返回最后一条或者满足条件的最后一条
     */
    private static void doLast() {
        Observable.just(1, 2, 3, 4, 5, 6, 7).last(integer -> integer < 5).subscribe(System.out::println);
    }

    /**
     * 只会返回第一条或者满足条件的第一条数据
     */
    private static void doFirst() {
        Observable.just(1, 2, 3, 4, 5, 6, 7).first(integer -> integer > 5).subscribe(System.out::println);
    }

    /**
     * 过滤数据，可以返回也可以过滤
     */
    private static void doFilter() {
        Observable.just(1, 2, 3, 4, 5, 6).filter(integer -> {
            // true会被发送出去，false则会被过滤掉
            return integer < 4;
        }).subscribe(System.out::println);
    }

    /**
     * 过滤某个索引的数据(同数组一样，从0开始)，并返回出来
     */
    private static void doElementAt() {
        Observable.just(1, 2, 3, 4, 5, 6).elementAt(2).subscribe(System.out::println);
    }

    /**
     * 去重
     */
    private static void doDistinct() {
        System.out.println("不会有重复");
        Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1).distinct().subscribe(System.out::println);
        System.out.println("有重复但是不会连续");
        Observable.just(1, 1, 2, 3, 4, 5, 5, 6, 2).distinctUntilChanged().subscribe(System.out::println);
    }

    /**
     * 限流--过滤的数据直接抛弃
     */
    private static void doDebounce() {
        // 通过时间限流
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            long sleep;
            try {
                for (int i = 0; i < 10; i++) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(i);
                    }
                    sleep = (i % 3 == 0) ? 100L : 300L;
                    Thread.sleep(sleep);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).throttleWithTimeout(200L, TimeUnit.MILLISECONDS).subscribe(System.out::println);

        // debounce限流--当然也可以类似上面的方式按时间限流
        Observable.interval(1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .debounce(aLong -> Observable.timer(aLong % 2 * 1500L, TimeUnit.MILLISECONDS))
            .subscribe(System.out::println);
    }
}
