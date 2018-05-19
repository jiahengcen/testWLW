package vending.lxuan.com.vendingmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import vending.lxuan.com.vendingmachine.R;
import vending.lxuan.com.vendingmachine.model.ListModel;

/**
 * Created by apple
 * 18/5/8
 */

public class PageAdapter extends BaseAdapter {
    private ListModel model;

    public PageAdapter(ListModel model) {
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.msg.size();
    }

    @Override
    public Object getItem(int i) {
        return model.msg.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Context ctx = viewGroup.getContext();
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.adapter_list, viewGroup, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(ctx).load(model.msg.get(i).headimgurl).into(viewHolder.iv_head);
        viewHolder.tv_name.setText(model.msg.get(i).nickname);
        viewHolder.tv_msg.setText(model.msg.get(i).content);
        return convertView;
    }

    static class ViewHolder {

        public TextView tv_name;
        public TextView tv_msg;
        public ImageView iv_head;

        ViewHolder(View view) {
            tv_name = view.findViewById(R.id.tv_name);
            tv_msg = view.findViewById(R.id.tv_msg);
            iv_head = view.findViewById(R.id.iv_head);
        }
    }
}
