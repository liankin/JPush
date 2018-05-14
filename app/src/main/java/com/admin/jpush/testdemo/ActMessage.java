package com.admin.jpush.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.admin.jpush.BaseApplication;
import com.admin.jpush.R;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息详情
 */

public class ActMessage extends AppCompatActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_message);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
        BaseApplication.setIsNotify(false);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constant.KEY)){
            MessageMode mode = (MessageMode)intent.getSerializableExtra(Constant.KEY);
            tvContent.setText(mode.getMsg());

            if(!TextUtils.isEmpty(mode.getExtra())){
                //json字符串转换为指定的mode
                JPushMode jPushMode = JSON.parseObject(mode.getExtra(), JPushMode.class);
            }
        }else{
            ToastUtil.showMessage("无消息");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDataChanged(MessageMode mode){
        if (mode != null){
            tvContent.setText(mode.getMsg());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.setIsNotify(true);
        EventBus.getDefault().unregister(this);
    }

}
