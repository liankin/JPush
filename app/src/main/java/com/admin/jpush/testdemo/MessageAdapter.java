package com.admin.jpush.testdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admin.jpush.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianglei on 2/4/17.
 */

public class MessageAdapter extends BaseAdapter<MessageMode> {

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_message, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MessageMode item = getItem(position);
        holder.tvContent.setText(item.getMsg());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ActMessage.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY,item);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
