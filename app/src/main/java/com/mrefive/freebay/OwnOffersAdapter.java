package com.mrefive.freebay;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrefive on 1/7/16.
 */
public class OwnOffersAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public OwnOffersAdapter(Context context, int resource) {
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
            //contactHolder.menuB = (ImageButton) row.findViewById(R.id.menuB);
            row.setTag(contactHolder);
        } else {

            contactHolder = (ContactHolder) row.getTag();
        }

        OwnOffers ownOffers = (OwnOffers) this.getItem(position);
        contactHolder.tv_title.setText(ownOffers.getName());
        contactHolder.tv_descr.setText(ownOffers.getDescr());

        //Bitmap menuButtonBackground = getContext().getResources().getDrawable(R.drawable.menuthreebars);
        //Bitmap resized = Bitmap.createScaledBitmap(menuButtonBackground, newWidth, newHeight, true);
        //manipulate views within row
        //contactHolder.menuButton.setBackground(Bitmap.createScaledBitmap(R.drawable.menuthreebars, (int) (R.drawable.menuthreebars.getWidth() * 0.8), (int) (R.drawable.menuthreebars.getHeight() * 0.8), true););

        return row;
    }

    static class ContactHolder {
        TextView tv_title, tv_descr;
        ImageButton menuB;

    }


}
