import rx.Observable;
import rx.Subscriber;

/**
 * 2.10 自定义操作符
 */
public class CustomCharacter {
    public static void main(String[] args) {
        // doLift();
        // doCompose();
    }

    /**
     * 作用在整个Observable上--Transformer对象---对Observable操作
     */
    private static void doCompose() {
        Observable.just(1, 2, 3, 4)
            .compose(integerObservable -> integerObservable.map(integer -> "transform:" + integer)
                .doOnNext(s -> System.out.println("doOnNext" + s)))
            .subscribe(System.out::println);
    }

    /**
     * 作用在Observable的数据上--多Subscriber操作
     */
    private static void doLift() {

        // 一定要检查是否注册状态
        Observable.Operator<String, String> operator = new Observable.Operator<String, String>() {
            @Override
            public Subscriber<? super String> call(Subscriber<? super String> subscriber) {
                return new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(throwable);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(">>>" + s);
                        }
                    }
                };
            }
        };

        Observable.just(1, 2, 3, 4)
            .map(integer -> "map1:" + integer)
            .lift(operator)
            .map(integer -> "map2:" + integer)
            .subscribe(System.out::println);

    }
}
