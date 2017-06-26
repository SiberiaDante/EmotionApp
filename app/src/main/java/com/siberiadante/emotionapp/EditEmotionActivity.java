package com.siberiadante.emotionapp;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.siberiadante.emotionapp.fragments.EmotionMainFragment;

/**
 * Created by SiberiaDante
 * Describe: 表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */

public class EditEmotionActivity extends AppCompatActivity {
    public static final String TAG = EditEmotionActivity.class.getSimpleName();
    private TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_edit_emotion);
        initView();
        initData();
    }

    private void initView() {
        mTvContent = (TextView) findViewById(R.id.tv_input_content);
    }

    private void initData() {
        initEmotionMainFragment();
    }

    private void initEmotionMainFragment() {
        EmotionMainFragment emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, null);
        emotionMainFragment.bindToContentView(mTvContent);//绑定当前页面控件
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotion_view_main, emotionMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
