package li.mybasedemo.activity;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import li.mybasedemo.R;
import li.mybasedemo.module.Student;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 创建时间: 2017/12/4
 * 创建人: Administrator
 * 功能描述:
 */

public class RxActivity extends BaseActivity {
    private final static String TAG = "RxActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_rx;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        List<Student> data = new ArrayList<>();
        data.add(new Student("te", 11));
        data.add(new Student("cc", 12));
        //使用Observable.create()创建被观察者
        Observable observable1 = Observable.from(data);
        //订阅
        observable1.map(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.getName();
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i(TAG, "on Error");
            }
        }, new Action0() {
            @Override
            public void call() {
                Log.i(TAG, "Completed");
            }
        });

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View view) {

    }
}
