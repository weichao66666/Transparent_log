package io.weichao.transparent_log.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.weichao.transparent_log.R;
import io.weichao.transparent_log.local_log.LocalLogFragment;
import io.weichao.transparent_log.local_log.LocalLogListener;
import io.weichao.transparent_log.util.FragmentUtil;

public class MainActivity extends AppCompatActivity implements MainActivityView, LocalLogListener {
    private MainFragment mMainFragment;
    private LocalLogFragment mLocalLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFragment = MainFragment.newInstance();
        final Bundle bundle = new Bundle();
        bundle.putString("url", "http://www.baidu.com");
        mMainFragment.setArguments(bundle);
        FragmentUtil.replaceFragment(getSupportFragmentManager(), R.id.main, mMainFragment);

        mLocalLogFragment = LocalLogFragment.newInstance();
        FragmentUtil.replaceFragment(getSupportFragmentManager(), R.id.log, mLocalLogFragment);
    }

    @Override
    public void hideLog() {
        mLocalLogFragment.hide();
    }

    @Override
    public void showLog() {
        mLocalLogFragment.show();
    }

    @Override
    public void clearLog() {
        mLocalLogFragment.clear();
    }

    @Override
    public void printLog(String msg) {
        mLocalLogFragment.log(0, msg);
    }

    @Override
    public void printLog(int level, String msg) {
        mLocalLogFragment.log(level, msg);
    }
}