import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 2.9 与Connectable Observable相关的操作符
 * 之前学的都是注册了才会发送，而现在这一类是只有connect就发送数据。
 */
public class ConnectableObservable {
    public static void main(String[] args) {
        // doPublish();// 无法打印
        // doRecount();//无法打印
        // doReplay();//无法打印
    }

    /**
     * 相当于public的加强版，可以缓存发送过的数据--最好限定好缓存大小
     */
    private static void doReplay() {
        rx.observables.ConnectableObservable<Long> replay =
            Observable.interval(1000L, TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread()).replay(2);
        replay.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(">>>" + aLong);
                if (aLong == 3) {
                    replay.subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            System.out.println("<<<" + aLong);
                        }
                    });
                }
            }
        });

        replay.connect();
    }

    /**
     * 和public相反，这个是反转成一个普通的Observable
     */
    private static void doRecount() {
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.newThread())
            .publish()
            .refCount()
            .subscribe(System.out::println);
    }

    /**
     * 讲一个普通的Observable转化为Connectable Observable
     */
    private static void doPublish() {
        rx.observables.ConnectableObservable<Long> publish =
            Observable.interval(1000L, TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread()).publish();

        publish.subscribe(aLong -> {
            System.out.println(">>>:" + aLong);
            if (aLong == 3) {
                publish.subscribe(aLong1 -> System.out.println("####:" + aLong1));
            }
        });

        // 上面虽然订阅了但是没有connect所以不发送数据
        Subscription connect = publish.connect();

        // 想要终止发送数据
        if (connect != null) {
            connect.unsubscribe();
        }

    }
}
