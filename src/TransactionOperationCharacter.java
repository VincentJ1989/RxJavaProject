import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 转化操作符
 * @author vincent
 */
public class TransactionOperationCharacter {
    public static void main(String[] args) {
        // doBuffer();
        // doFlatMap();
        // doGroupBy();
        // doMap();
        // doCast();
        // doScan();
        doWindow();
    }

    private static void doWindow() {
        // 按window数目输出
        Observable.range(1, 9).window(3).subscribe(integerObservable -> {
            System.out.println(integerObservable.getClass().getName());
            integerObservable.subscribe(System.out::println);
        });

        // 按时间输出
        System.out.println("windowByTime");
        Observable.interval(1L, TimeUnit.SECONDS, Schedulers.trampoline()).window(3L, TimeUnit.SECONDS).subscribe(
            longObservable -> {
                System.out.println(longObservable.getClass().getName());
                longObservable.subscribe(aLong -> System.out.println(String.format("windowByTime:%d", aLong)));
            });

    }

    private static void doScan() {
        List<Integer> intList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            intList.add(2);
        }

        Observable.from(intList).scan((x, y) -> {
            System.out.println(x);
            System.out.println(y);
            return x * y;
        }).subscribe(System.out::println);
    }

    private static void doCast() {

    }

    private static void doMap() {
        Observable.just(1, 2, 3, 4).map(integer -> String.format("Item%d", integer)).subscribe(System.out::println);
    }

    private static void doGroupBy() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8).groupBy(integer -> integer % 3).subscribe(
            integerIntegerGroupedObservable -> {
                if (integerIntegerGroupedObservable.getKey() == 0) {
                    integerIntegerGroupedObservable.subscribe(integer -> {
                        System.out.println("被3整除的数字有:");
                        System.out.println(integer);
                    });
                }
            });
    }

    private static void doFlatMap() {
        Observable.just(1, 2, 3, 4)
            .flatMap((Func1<Integer, Observable<Integer>>) integer -> Observable.just(integer << 1))
            .subscribe(System.out::println);
    }

    private static void doBuffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).buffer(3, 4).subscribe(integers -> {
            for (Integer integer : integers) {
                System.out.println(integer);
            }
            System.out.println("------------");
        });
    }
}
