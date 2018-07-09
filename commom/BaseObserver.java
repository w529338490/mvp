package robot.nsk.com.robot_app.commom;



import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e.toString());
    }
}

