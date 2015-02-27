package wificontrol.lichuang.com.wificontrol.device.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/2/12.
 */
public class CmdParcelable implements Parcelable {
    public String mac_addr;
    public byte[] cmd_data;

    public CmdParcelable(Parcel parcel){
        this.mac_addr = parcel.readString();
        parcel.readByteArray(cmd_data);
    }

    public CmdParcelable(String mac,byte[] cmd_data){
        this.mac_addr =mac;
        this.cmd_data = cmd_data;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mac_addr);
        dest.writeByteArray(cmd_data);
    }

    public static final Parcelable.Creator<CmdParcelable> CREATOR = new Parcelable.Creator<CmdParcelable>() {

        @Override
        public CmdParcelable createFromParcel(Parcel source) {
            return new CmdParcelable(source);
        }

        @Override
        public CmdParcelable[] newArray(int size) {
            return new CmdParcelable[0];
        }
    };
}
