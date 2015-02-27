package wificontrol.lichuang.com.wificontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import wificontrol.lichuang.com.wificontrol.R;

/**
 * Created by Administrator on 2014/12/26.
 */
public class ModelAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> data;
    private LayoutInflater inflater;

    public ModelAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            view = inflater.inflate(R.layout.model_item, null);

            holder.model_img = (ImageView) view.findViewById(R.id.model_img);
            holder.model_tv = (TextView) view.findViewById(R.id.model_tv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.model_img.setBackgroundResource((int) data.get(position).get("model_img"));
        holder.model_tv.setText((String) data.get(position).get("model_tv"));
        return view;
    }

    class ViewHolder {
        public ImageView model_img;
        public TextView model_tv;
    }
}
