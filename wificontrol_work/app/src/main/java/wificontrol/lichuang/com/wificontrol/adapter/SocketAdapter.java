package wificontrol.lichuang.com.wificontrol.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import wificontrol.lichuang.com.wificontrol.R;

public class SocketAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map<String, Object>> dataList;
	
	public SocketAdapter(Context context,List<Map<String, Object>> dataList) {
		this.inflater = LayoutInflater.from(context);
		this.dataList=dataList;
	}



    public void SetData(List<Map<String,Object>> dataList){
            this.dataList = dataList;
    }

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder = new ViewHolder();

		if (view == null) {
			view = inflater.inflate(R.layout.socket_item, null);

			holder.socket_img = (ImageView) view.findViewById(R.id.socket_img);
			holder.socket_name = (TextView) view.findViewById(R.id.socket_name);
            holder.socket_modbus = (TextView) view.findViewById(R.id.socket_modbus);
			holder.socket_state = (TextView) view.findViewById(R.id.socket_state);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.socket_img.setBackgroundResource((int) dataList.get(arg0).get("socket_img"));
		holder.socket_name.setText((String) dataList.get(arg0).get("socket_name"));
        holder.socket_modbus.setText((String) dataList.get(arg0).get("socket_modbus"));
		holder.socket_state.setText((String) dataList.get(arg0).get("socket_state"));
		return view;
	}

     class ViewHolder {
        public ImageView socket_img;
        public TextView socket_name;
        public TextView socket_modbus;
        public TextView socket_state;
    }

}
