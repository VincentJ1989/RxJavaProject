import rx.Observable;
import rx.Subscriber;

/**
 * 错误处理操作符
 */
public class ErrorHandleOperationCharacter {
    public static void main(String[] args) {
        // doErrorReturn();
        // doErrorReturnResumeNext();
        // doExceptionResumeNext(true);
        doRetry();
    }

    /**
     * 发生错误，重新发送，可以定制发送的次数，最后还是发送失败在，则将错误返回
     */
    private static void doRetry() {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            for (int i = 0; i < 6; i++) {
                if (i == 3) {
                    subscriber.onError(new Exception("Exception:" + i));
                } else {
                    subscriber.onNext("OnNext:" + i);
                }
            }
        }).retry(3).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError>>" + throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext>>>" + s);
            }
        });
    }

    /**
     * 与ErrorResumeNext差不都，不过这个会对onError抛出的数据类型做判断，
     * 如果是Exception会用另一个Observable代替原先的继续发送，否则将错误分发给subscribe
     */
    private static void doExceptionResumeNext(boolean pIsException) {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            for (int i = 0; i < 6; i++) {
                if (i < 4) {
                    subscriber.onNext(String.format("OnNext:%d", i));
                } else if (pIsException) {
                    subscriber.onError(new Exception("Exception"));
                } else {
                    subscriber.onError(new Throwable("Throwable"));
                }
            }
        }).onExceptionResumeNext(Observable.just("Exception的Observable")).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError>>" + throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext>>" + s);
            }
        });
    }

    /**
     * 如其名，发生错误时会停止之前的Observable，然后创建新的继续发送数据(这里的数据不一定是之前未发送的)
     */
    private static void doErrorReturnResumeNext() {
        createObservable().onErrorResumeNext(Observable.just("这是", "新的", "数据")).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onErrorReturnResume-Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onErrorReturnResume-OnError:" + throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("onErrorReturnResume-OnNext:" + s);
            }
        });
    }

    /**
     * 当出现错误的时候，会发送一个自定义的数据，正常结束整个流
     */
    private static void doErrorReturn() {
        createObservable().onErrorReturn(throwable -> "OnErrorReturn").subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onErrorReturn-Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onErrorReturn-OnErro:" + throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("onErrorReturn-OnNext:" + s);
            }
        });

    }

    public static Observable createObservable() {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            for (int i = 0; i < 5; i++) {
                if (i < 3) {
                    subscriber.onNext("OnNext:" + i);
                } else {
                    subscriber.onError(new Throwable("Throw error"));
                }
            }
        });
    }

}
