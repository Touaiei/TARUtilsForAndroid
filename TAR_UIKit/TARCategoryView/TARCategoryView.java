package com.huanwei.TAR_UtilsForAndroid.TAR_UIKit.TARCategoryView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/2/21.
 */

public class TARCategoryView extends GridView {
    public TARCategoryViewAdapter categoryViewAdapter;
    public List<TARCategoryViewBean> categoryDataList = new ArrayList<>();
    public Context context;

    public TARCategoryViewAdapter getCategoryViewAdapter() {
        return categoryViewAdapter;
    }

    public void setCategoryViewAdapter(TARCategoryViewAdapter categoryViewAdapter) {
        this.categoryViewAdapter = categoryViewAdapter;
    }

    public List<TARCategoryViewBean> getCategoryDataList() {
        return categoryDataList;
    }

    public void setCategoryDataList(List<TARCategoryViewBean> categoryDataList) {
        this.categoryDataList = categoryDataList;
    }

    public TARCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TARCategoryView(Context context) {
        super(context);
    }

    public TARCategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    public void setAdapter(){
        setAdapter(categoryViewAdapter);
    }

    public void initAdapter(Context context,List<TARCategoryViewBean> list){
        this.context = context;
        this.categoryDataList = list;
        this.categoryViewAdapter = new TARCategoryViewAdapter(context, categoryDataList);
    }
    public void notifyDataSetChanged(){
        categoryViewAdapter.notifyDataSetChanged();
    }
}
