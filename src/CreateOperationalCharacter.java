import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * 创建操作符
 * @author vincent
 */
public class CreateOperationalCharacter {
    public static void main(String[] args) {
        createByRange();
        createByDefer();
        createByFrom();
        createByInterval();
        createByRepeat();
    }

    private static void createByRepeat() {
        Observable.just(1, 2, 3).repeat(3).subscribe(System.out::println);
    }

    private static void createByInterval() {
        Observable.interval(1L, TimeUnit.SECONDS, Schedulers.trampoline()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });
    }

    private static void createByFrom() {
        Observable.from(Arrays.asList("a", "b", "c", "d")).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    private static void createByDefer() {
        Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(System.currentTimeMillis());
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });

        Observable.defer(() -> Observable.just(System.currentTimeMillis())).subscribe(System.out::println);
    }

    private static void createByRange() {
        Observable.range(0, 5).subscribe(System.out::println);
    }

}
