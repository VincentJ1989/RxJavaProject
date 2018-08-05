import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * 组合操作符--主要用于汇总各种结果的场景.
 * @author vincent
 */
public class CombineOperationCharacter {
    public static void main(String[] args) {
        // TODO: 2018/8/5 不能打印
        // doCombineLast();

        // doJoin();
        // doMerge();
        // doStartWith();
        // TODO: 2018/8/5 输出结果与课本理论值不一致
        // doSwitch();

        // doZip();
        // doZipWith();
    }

    /**
     * 把多个Observable的数据按顺序组合起来，和join不同，zip智能组合一次，而join可以的多次组合，
     * 而且zip的是有序的，最终的数量是取最少的Observable的发送数量.<br></br>
     * zipWith原理一样.
     */
    private static void doZip() {
        Observable.zip(createZipObservable(2), createZipObservable(3), createZipObservable(4),
            new Func3<String, String, String, String>() {
                @Override
                public String call(String s, String s2, String s3) {
                    return s + "--" + s2 + "--" + s3;
                }
            }).subscribe(System.out::println);
    }

    private static void doZipWith() {
        createZipObservable(2).zipWith(createZipObservable(3), new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + "--" + s2;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                System.out.println("zipWith:" + o);
            }
        });
    }

    private static Observable createZipObservable(int index) {
        return Observable.interval(100L, TimeUnit.MILLISECONDS, Schedulers.trampoline()).take(index).map(
            new Func1<Long, String>() {
                @Override
                public String call(Long aLong) {
                    return index + ":" + aLong;
                }
            });
    }

    /**
     * 只对小小的Observable感兴趣--switchOnNext
     */
    private static void doSwitch() {
        Observable.switchOnNext(Observable.interval(3000L, TimeUnit.MILLISECONDS, Schedulers.trampoline()).take(3).map(
            new Func1<Long, Observable<?>>() {
                @Override
                public Observable<?> call(Long index) {
                    return Observable.interval(1000L, 1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
                        .take(5)
                        .map(new Func1<Long, String>() {
                            @Override
                            public String call(Long aLong) {
                                return index + "--" + aLong;
                            }
                        });
                }
            })).subscribe(System.out::println);

    }

    /**
     * 在源Observable之前插入新的数据
     */
    private static void doStartWith() {
        Observable.just(1, 2, 3, 4, 5).startWith(-1, 0).subscribe(System.out::println);
    }

    /**
     * 整合发送数据--可能数据有交错，可以使用concat避免<br></br>
     * 还有个mergeDelayError，可以避免发送中间因error而中断
     */
    private static void doMerge() {
        Observable.merge(Observable.just(1, 2, 3, 4), Observable.just("a", "b", "c")).subscribe(System.out::println);
    }

    /**
     * 根据时间窗口来组合数据
     */
    private static void doJoin() {

        Observable.just("a", "b", "c", "d")
            .join(Observable.just(1L, 2L, 3L, 4L), new Func1<String, Observable<Long>>() {
                @Override
                public Observable<Long> call(String s) {
                    return Observable.timer(1000L, TimeUnit.MILLISECONDS);
                }
            }, new Func1<Long, Observable<Long>>() {
                @Override
                public Observable<Long> call(Long s) {
                    return Observable.timer(1000L, TimeUnit.MILLISECONDS);
                }
            }, new Func2<String, Long, String>() {
                @Override
                public String call(String left, Long right) {
                    return left + ":" + right;
                }
            })
            .subscribe(System.out::println);
    }

    /**
     * 将一系列的数据按一定规则组装发送出去(只组装最新的数据)<br></br>
     * 前提：<br></br>
     * <li>1:所有的Observable必须全部发送过数据，否则combineLast则会一直等待</li>
     * <li>2:满足1的条件下，任意的一个Observable发送数据，combineLast就会执行</li>
     */
    private static void doCombineLast() {
        Observable
            .combineLatest(createObservable(1), createObservable(2),
                (aLong, aLong2) -> ("left=" + aLong + " right=" + aLong2))
            .subscribe(System.out::println);
    }

    private static Observable<Long> createObservable(int index) {
        return Observable.interval(500L * index, TimeUnit.MILLISECONDS, Schedulers.trampoline());

    }
}
