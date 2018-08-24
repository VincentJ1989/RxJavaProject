import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 2.7 条件操作符
 */
public class ConditionCharacter {
    public static void main(String[] args) {
        // doAll();
        // doArm();//线程问题不好模拟
        // doContain();
        // isEmpty();
        // doDefaultEmpty();
        // doSequenceEqual();
        // doSkipUntil();
        // doSkipWhile();
        // doTakeUntil();
        doTakeWhile();
    }

    /**
     * 与skipWhile相反
     */
    private static void doTakeWhile() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .takeWhile(aLong -> aLong < 5)
            .subscribe(System.out::println);
    }

    /**
     * 和skipUntil相反
     */
    private static void doTakeUntil() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .takeUntil(Observable.timer(5000L, TimeUnit.MILLISECONDS, Schedulers.newThread()))
            .subscribe(System.out::println);
    }

    /**
     * 根据一个函数判断是否跳过，true就跳过；反之则发送。
     */
    private static void doSkipWhile() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .skipWhile(aLong -> aLong < 5)
            .subscribe(System.out::println);
    }

    /**
     * 根据一个Observable判断跳过一些数据：当它发送数据的时候，正常发送；没发送都跳过
     */
    private static void doSkipUntil() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .skipUntil(Observable.timer(5000L, TimeUnit.MILLISECONDS, Schedulers.newThread()))
            .subscribe(System.out::println);
    }

    /**
     * 判断2个Observable发送的数据序列是否相同
     */
    private static void doSequenceEqual() {
        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3)).subscribe(System.out::println);
    }

    /**
     * 提供空数据默认值，有发送就按发送，没有则发送默认
     */
    private static void doDefaultEmpty() {
        Observable.empty().defaultIfEmpty("empty").subscribe(System.out::println);
        Observable.just(1).defaultIfEmpty(2).subscribe(System.out::println);
    }

    /**
     * 是否发送过数据
     */
    private static void isEmpty() {
        Observable.just(1).isEmpty().subscribe(System.out::println);
        Observable.empty().isEmpty().subscribe(System.out::println);
    }

    /**
     * 判断是否包含
     */
    private static void doContain() {
        Observable.just(1, 2, 3, 4).contains(3).subscribe(System.out::println);
        Observable.just(1, 2, 3, 4).contains(5).subscribe(System.out::println);
    }

    /**
     * 将至多9个Observable集合，抢占唯一输出
     *
     */
    private static void doArm() {
        Observable
            .amb(Observable.just(1, 2, 3).delay(3000L, TimeUnit.MILLISECONDS, Schedulers.newThread()),
                Observable.just(4, 5, 6).delay(2000L, TimeUnit.MILLISECONDS, Schedulers.newThread()),
                Observable.just(7, 8, 9).delay(1000L, TimeUnit.MILLISECONDS, Schedulers.newThread()))
            .subscribe(System.out::println);
        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {

        }
    }

    /**
     * 对发送的所有数据进行判断，都满足则true；反正false
     */
    private static void doAll() {
        Observable.just(1, 2, 3, 4, 5).all(integer -> integer < 6).subscribe(System.out::println);

        Observable.just(1, 2, 3, 4, 5).all(integer -> integer < 3).subscribe(System.out::println);
    }
}
