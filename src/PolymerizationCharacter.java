import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func0;

/**
 * 2.8聚合操作符
 */
public class PolymerizationCharacter {
    public static void main(String[] args) {
        // doConcat();
        // doCount();
        // doReduce();
        doCollect();
    }

    /**
     * 和reduce类似，但是二者目的不同，collect是将数据收集到一个数据结构里，然后把整个数据结构发送出去
     *例子是发送出去的数据存到一个ArrayList，然后把ArrayList发送出去
     */
    private static void doCollect() {

        Observable.just(1, 2, 3, 4, 5, 6).collect((Func0<ArrayList<Integer>>) ArrayList::new, ArrayList::add).subscribe(
            System.out::println);

        // Observable.just(1,2,3,4,5,6).collect(new Func0<ArrayList<Integer>>() {
        // @Override
        // public ArrayList<Integer> call() {
        // return new ArrayList<>();
        // }
        // }, new Action2<ArrayList<Integer>, Integer>() {
        // @Override
        // public void call(ArrayList<Integer> list, Integer integer) {
        // list.add(integer);
        // }
        // }).subscribe(new Action1<ArrayList<Integer>>() {
        // @Override
        // public void call(ArrayList<Integer> list) {
        // System.out.println(list);
        // }
        // });
    }

    /**
     * 用一个函数接收发送的数据和计算的结果，作为下次的输入数据
     */
    private static void doReduce() {
        Observable.just(1, 2, 3, 4).reduce((integer, integer2) -> integer + integer2).subscribe(System.out::println);
    }

    /**
     * 统计发送了多少数据
     */
    private static void doCount() {
        Observable.just(1, 2, 3, 4, 5, 6).count().subscribe(System.out::println);
    }

    /**
     * 把多个Observable合并成一个，并严格的按顺序输出<br></br>
     * 类似的有startWith(插入)和merge(无序输出)
     */
    private static void doConcat() {
        Observable.concat(Observable.just(1, 2, 3), Observable.just(7, 8, 9), Observable.just(4, 5, 6))
            .subscribe(System.out::println);
    }
}
