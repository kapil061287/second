package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Coupon;

import java.util.List;

public class CouponAdapter  extends BaseAdapter{

    private List<Coupon> coupons;
    private Context context;

    public CouponAdapter(List<Coupon> coupons, Context context)
    {
        this.coupons=coupons;
        this.context=context;
    }

    @Override
    public int getCount() {
        return coupons.size();
    }

    @Override
    public Object getItem(int i) {
        return coupons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return coupons.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Coupon coupon=coupons.get(i);
        if(view!=null){
            ViewHolder viewHolder= (ViewHolder) view.getTag();
            bindViewHolder(viewHolder, coupon);
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.content_coupon_list_layout, viewGroup, false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.coupenKey=view.findViewById(R.id.coupen_key);
            viewHolder.coupenDesc=view.findViewById(R.id.coupen_desc);
            view.setTag(viewHolder);
            bindViewHolder(viewHolder, coupon);
        }


       return view;
    }

    class ViewHolder{
        TextView coupenKey;
        TextView coupenDesc;
    }

    void bindViewHolder(ViewHolder viewHolder, Coupon coupon){
        viewHolder.coupenDesc.setText(coupon.getCouponDesc());
        viewHolder.coupenKey.setText(coupon.getCouponKey());
    }
}