package com.siberiadante.emotionapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.siberiadante.emotionapp.R;
import com.siberiadante.emotionapp.adapter.HorizontalRecyclerviewAdapter;
import com.siberiadante.emotionapp.adapter.NoHorizontalScrollerVPAdapter;
import com.siberiadante.emotionapp.model.ImageModel;
import com.siberiadante.emotionapp.utils.EmotionUtils;
import com.siberiadante.emotionapp.utils.GlobalOnItemClickManagerUtils;
import com.siberiadante.emotionapp.utils.SharedPreferencedUtils;
import com.siberiadante.emotionapp.utils.SpanStringUtils;
import com.siberiadante.emotionapp.widget.EmotionKeyboard;
import com.siberiadante.emotionapp.widget.NoHorizontalScrollerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiberiaDante
 * Describe:
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */
public class EmotionMainFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = EmotionMainFragment.class.getSimpleName();
    private CheckBox mCBEmotionBtn;
    private EditText mEdtContent;
    private Button mBtnSend;
    private NoHorizontalScrollerViewPager mNoHorizontalVP;
    //底部水平tab
    private RecyclerView mBottomRecyclerView;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    private List<Fragment> fragments = new ArrayList<>();

    private View contentView;
    private EmotionKeyboard mEmotionKeyboard;
    private GlobalOnItemClickManagerUtils globalOnItemClickManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_emotion_main, container, false);
        initView(layout);
        //初始化EmotionKeyboard
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(layout.findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(((EditText) layout.findViewById(R.id.bar_edit_text)))//判断绑定那种EditView
                .bindToEmotionButton(layout.findViewById(R.id.emotion_button))//绑定表情按钮
                .build();
        initData();
//        点击表情的全局监听管理类
        globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance();
        //绑定EditText
        globalOnItemClickManager.attachToEditText(mEdtContent);
        return layout;
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }

    private void initView(View layout) {

        mCBEmotionBtn = ((CheckBox) layout.findViewById(R.id.emotion_button));
        mEdtContent = ((EditText) layout.findViewById(R.id.bar_edit_text));
        mBtnSend = ((Button) layout.findViewById(R.id.bar_btn_send));
        mNoHorizontalVP = ((NoHorizontalScrollerViewPager) layout.findViewById(R.id.vp_emotionview_layout));
        mBottomRecyclerView = ((RecyclerView) layout.findViewById(R.id.recyclerview_horizontal));
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_btn_send:
                Log.d(TAG, "onClick: " + mEdtContent.getText().toString());

                ((TextView) contentView).setText(SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
                        getActivity(), ((TextView) contentView), mEdtContent.getText().toString()));
                mEdtContent.setText("");
                break;
        }
    }

    private void initData() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.drawable.ic_emotion);
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {//创建其他的表情fragment，不需要可以注释
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.drawable.ic_plus);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }


        //记录底部默认选中第一个
        CurrentPosition = 0;
        SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        mBottomRecyclerView.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        mBottomRecyclerView.setAdapter(horizontalRecyclerviewAdapter);
        mBottomRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SharedPreferencedUtils.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                mNoHorizontalVP.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });


    }

    /**
     * 创建其他的表情fragment，不需要可以注释
     */
    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotionComplateFragment f1 = (EmotionComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        Bundle b = null;
        for (int i = 0; i < 7; i++) {
            b = new Bundle();
            b.putString("Interge", "Fragment-" + i);
            FragmentOther fg = FragmentOther.newInstance(FragmentOther.class, b);
            fragments.add(fg);
        }

        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), fragments);
        mNoHorizontalVP.setAdapter(adapter);
    }

    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        globalOnItemClickManager.unAttachToEditText();
    }
}
