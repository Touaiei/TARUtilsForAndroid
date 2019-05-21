package com.huanwei.TAR_UtilsForAndroid.TAR_UIKit.TARCategoryView;

/**
 * Created by Administrator on 2019/2/21.
 */

public class TARCategoryViewBean {
    private String text;//文字
    private String image;//图标
    private int itemIndex;//
    private String type;//
    private boolean isShowBadge = false;//是否显示徽章
    private int badgeNumber = 0;//徽章数量

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int index) {
        this.itemIndex = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShowBadge() {
        return isShowBadge;
    }

    public void setShowBadge(boolean showBadge) {
        isShowBadge = showBadge;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }
}
