/*
this class is used to layout structure and build a recyclerview

 */
package com.devil07.divyang.okhttplib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StringLoader;

import java.util.List;

public class dataadaptor extends RecyclerView.Adapter<dataadaptor.MyViewHolder> {

    private List<data> dataList;
    private Context context;
    private LayoutInflater mInflater;

    public dataadaptor(Context context, List<data> dataList) {   //parameterized constructor to inilize dataadaptor
        this.dataList = dataList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mainadress1, mainadress2;
        public ImageView image;
        public RelativeLayout rv;

        public MyViewHolder(View view) {
            super(view);
            mainadress1 = (TextView) view.findViewById(R.id.textViewName);
            mainadress2 = (TextView) view.findViewById(R.id.textViewEmail);
            image = (ImageView) view.findViewById(R.id.imageView);
            rv=(RelativeLayout)view.findViewById(R.id.rel);
            rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = itemView.getContext();
                    int position=getAdapterPosition();
                    String pos= String.valueOf(position+1);
                    Toast.makeText(context,"position ="+ position,Toast.LENGTH_SHORT).show();
                    Bundle basket = new Bundle();   /*
                                                      we need to send the position of the dataelement of the recycler view for new url as
                                                      explained new url being url/centerid which is equal to position hence used bundle basket
                                                      it is same as get function and stores info of this activity in basket used by other activity
                                                      in this case it is used in center class
                                                    */
                    basket.putString("key",pos);
                    Intent i=new Intent(context,Center.class);
                    i.putExtras(basket);
                    context.startActivity(i);

                }
            });

        }
    }

/*
viewholder class is used to give a layout to the recycler view
 */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);    //R.layout.row gives us one data element of recycler view

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        data gym = dataList.get(position);
        byte[] bytes=gym.getImg();
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        holder.mainadress1.setText(gym.getAdress1());
        holder.mainadress2.setText(gym.getAdress2());
        holder.image.setImageBitmap(bmp);

      //code below is no longer need as image formed as a byte array and as bitmap so can be loaded using that

        /* Glide.with(context)                 // get context of this activity
                .load(gym.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.image);   // into what you want */


    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}