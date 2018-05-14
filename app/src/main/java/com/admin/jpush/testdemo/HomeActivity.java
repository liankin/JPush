package com.admin.jpush.testdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.admin.jpush.BaseApplication;
import com.admin.jpush.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 首页
 */
public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private MessageAdapter messageAdapter;
    private List<MessageMode> msgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        //获得注册的key
        String rid = JPushInterface.getRegistrationID(BaseApplication.getAppContext());
        ToastUtil.showMessage("RegistrationID：" + rid);

        messageAdapter = new MessageAdapter(this);
        listView.setAdapter(messageAdapter);
        getListData();
    }

    private void getListData(){
        for(int i=0;i<3;i++){
            MessageMode messageMode = new MessageMode();
            messageMode.setMsg(i+"_message");
            msgList.add(messageMode);
        }
        messageAdapter.initData(Constant.PAGE_START,msgList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDataChanged(MessageMode mode){
        if (mode != null){
            msgList.add(mode);
            messageAdapter.initData(Constant.PAGE_START,msgList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
