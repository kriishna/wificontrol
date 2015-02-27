package wificontrol.lichuang.com.wificontrol.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.activity.SocketManageMainConnect;

/**
 * Created by Administrator on 2014/12/27.
 */
public class SocketConnectModelAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data;
    private LayoutInflater li;
    private Context context;

    public SocketConnectModelAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.data = data;
        this.li = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder = new Holder();
        if (view == null) {
            view = li.inflate(R.layout.socket_connect_model_item, null);

            holder.socket_connect_model_img = (ImageView) view.findViewById(R.id.socket_connect_model_img);
            holder.socket_connect_model_tv = (TextView) view.findViewById(R.id.socket_connect_model_tv);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.socket_connect_model_img.setBackgroundResource((int) data.get(position).get("socket_connect_model_img"));
        holder.socket_connect_model_tv.setText((String) data.get(position).get("socket_connect_model_tv"));

        ImageView socket_connect_model_ib=(ImageView)view.findViewById(R.id.socket_connect_model_ib);
//        socket_connect_model_ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(context,SocketManageMainConnect.class);
//                context.startActivity(intent);
//            }
//        });

        return view;
    }

    public class Holder {
        public ImageView socket_connect_model_img;
        public TextView socket_connect_model_tv;

    }

}
