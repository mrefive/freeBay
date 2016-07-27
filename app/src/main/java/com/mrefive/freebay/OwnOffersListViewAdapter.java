package com.mrefive.freebay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mrefive on 6/9/16.
 */
public class OwnOffersListViewAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public OwnOffersListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(OwnOffers object) {
        super.add(object);
        list.add(object);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        //contact holder is actually for ownOffers
        ContactHolder contactHolder = new ContactHolder();

        View row = convertView;
        if(row==null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.profile_list_view_layout, parent, false);
            contactHolder.tv_title = (TextView) row.findViewById(R.id.profLVname);
            contactHolder.tv_descr = (TextView) row.findViewById(R.id.profLVdescr);
            contactHolder.tv_dateend = (TextView) row.findViewById(R.id.profTVdateend);
            contactHolder.iv_image = (ImageView) row.findViewById(R.id.profimageview);
            row.setTag(contactHolder);
        } else {
            contactHolder = (ContactHolder) row.getTag();
        }

        OwnOffers ownOffers = (OwnOffers) this.getItem(position);
        contactHolder.tv_title.setText(ownOffers.getName());
        contactHolder.tv_descr.setText(ownOffers.getDescr());
        contactHolder.tv_dateend.setText(ownOffers.getDateend());
        contactHolder.iv_image.setImageBitmap(ownOffers.getImagesmall());

        return row;
    }

    static class ContactHolder {
        TextView tv_title, tv_descr, tv_dateend;
        ImageView iv_image;

    }

}
