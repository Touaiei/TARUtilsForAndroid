package com.huanwei.TAR_UtilsForAndroid.TAR_UIKit.TARCategoryView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanwei.huanwei.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/2/21.
 */

public class TARCategoryViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<TARCategoryViewBean> list;

    public TARCategoryViewAdapter(Context context, List<TARCategoryViewBean> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
//            convertView = layoutInflater.inflate(R.layout.tar_categoryv_view_item, null);
            convertView= layoutInflater.inflate(R.layout.tar_categoryv_view_item, parent,false);//传入parent设置的高度和宽度就会起作用了。

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TARCategoryViewBean bean = list.get(position);
        convertView.setMinimumHeight(100);
        viewHolder.text.setText(bean.getText());
        viewHolder.img.setImageResource(Integer.parseInt(bean.getImage()));
        viewHolder.point.setText(bean.getBadgeNumber()+"");
        if (bean.isShowBadge()){
            viewHolder.point.setVisibility(View.VISIBLE);
        }else {
            viewHolder.point.setVisibility(View.GONE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.point)
        TextView point;
        @BindView(R.id.text)
        TextView text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
