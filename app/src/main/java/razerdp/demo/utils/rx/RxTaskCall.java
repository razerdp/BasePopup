package razerdp.demo.utils.rx;

/**
 * Created by 大灯泡 on 2019/4/10
 * <p>
 * Description：
 */
public abstract class RxTaskCall<T> {

    public abstract T doInBackground();

    public abstract void onResult(T result);

    public void onError(Throwable e){

    }
}
