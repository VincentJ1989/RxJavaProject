import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 辅助操作符
 */
public class AssistantCharacter {
    public static void main(String[] args) {
        // doDelay();
        // doMaterialize();
        // doTimeInterval();
        // doTimeOut();
        doUsing();
    }

    /**
     * 创建一个Observable生命周期内的资源，当Observable终止的时候，这个资源也会被销毁
     */
    private static void doUsing() {
        Observable.using(new Func0<Animal>() {
            @Override
            public Animal call() {
                return new Animal();
            }
        }, new Func1<Animal, Observable<?>>() {
            @Override
            public Observable<?> call(Animal animal) {
                return Observable.timer(5000L, TimeUnit.MILLISECONDS, Schedulers.trampoline());
            }
        }, new Action1<Animal>() {
            @Override
            public void call(Animal animal) {
                animal.release();
            }
        }).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("onNext");
            }
        });
    }

    /**
     * 一种是直接在超时的时候发出一个错误事件<br></br>
     * 一种是在发生错误的时候继续发送数据(可能是其他的数据)
     */
    private static void doTimeOut() {
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            for (int i = 0; i < 4; i++) {
                try {
                    Thread.sleep(i * 100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(i);
            }

            subscriber.onCompleted();
        }).timeout(200L, TimeUnit.MILLISECONDS
        // ,Observable.just(9,8,7)
        ).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext:" + integer);
            }
        });
    }

    /**
     * 拦截数据，封装成TimeInterval(包含发送的原始数据+和上一个数据的间隔时间--对于第一个的时间间隔是订阅到其发送的时间)
     * 本质上和Materialize一样<br></br>
     * 同样的，他也有一个TimeStamp,封装的是原始数据和发送数据的时间戳
     */
    private static void doTimeInterval() {
        Observable.just(1, 2, 3).timeInterval().subscribe(integerTimeInterval -> {
            System.out.println(integerTimeInterval.getValue() + ":" + integerTimeInterval.getIntervalInMilliseconds());
        });
    }

    /**
     * 转化为Notification对象---dematerialize是前者的逆过程
     */
    private static void doMaterialize() {
        Observable.just(1, 2, 3).materialize().subscribe(integerNotification -> System.out
            .println(integerNotification.getValue() + ":" + integerNotification.getKind()));
    }

    /**
     * 延迟-还有个delaySubscription(延迟注册)
     */
    private static void doDelay() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .delay(5000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.trampoline())
            .subscribe(aLong -> {
                System.out.println(aLong);
            });
        // Observable.create((Observable.OnSubscribe<Long>) subscriber -> {
        // System.out.println("准备发送数据" + getCurrentSystemTime());
        // for (int i = 0; i < 2; i++) {
        // subscriber.onNext(getCurrentSystemTime());
        // }
        // })
        // .subscribeOn(Schedulers.trampoline())
        // .delay(3000L, TimeUnit.MILLISECONDS)
        //
        // .subscribe(new Subscriber<Long>() {
        // @Override
        // public void onCompleted() {
        // System.out.println("onCompleted");
        // }
        //
        // @Override
        // public void onError(Throwable throwable) {
        // System.out.println("onError:" + throwable.getMessage());
        // }
        //
        // @Override
        // public void onNext(Long aLong) {
        // System.out.println("onNext:" + aLong);
        // }
        // });

    }

    public static long getCurrentSystemTime() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * 辅助操作符Using使用的资源
     */
    public static class Animal {
        public Animal() {
            System.out.println("创建animal");
            Observable.interval(1000L, TimeUnit.MILLISECONDS).subscribe(subscriber);
        }

        public void release() {
            System.out.println("animal release");
            subscriber.unsubscribe();
        }

        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println("吃东西");
            }
        };
    }
}
