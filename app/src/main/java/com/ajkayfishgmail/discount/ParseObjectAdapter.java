package com.ajkayfishgmail.discount;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ajkay_000 on 4/10/2015.
 */

public class ParseObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<ParseObject> ParseList;
    public String BusinassName;
    public float Longitude;
    public float Latitude;
    public int discountInt;
   // selectedListener listen;



    public ParseObjectAdapter(ArrayList<ParseObject> ParseList1) //selectedLi)
    {
        ParseList = ParseList1;
    }



    private class  MyHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
    {
        public TextView Title;
        public TextView discount;

        public MyHolder(View itemView)
        {
            super(itemView);
            Title = (TextView)itemView.findViewById(R.id.display);
            discount = (TextView)itemView.findViewById(R.id.Discount);

        }

        @Override
        public void onClick(View v)
        {
            TextView t = (TextView) v;

        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell,
                viewGroup, false);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        MyHolder tmp = (MyHolder) viewHolder;
        BusinassName = ParseList.get(i).get("Name").toString();
        tmp.Title.setText(BusinassName);
        String discountString = ParseList.get(i).get("Discount").toString();
        discountInt = Integer.parseInt(discountString);
        //tmp.discount.setText(discountInt);
    }

    @Override
    public int getItemCount()
    {
        return ParseList.size();
        //return 1;
    }
}
