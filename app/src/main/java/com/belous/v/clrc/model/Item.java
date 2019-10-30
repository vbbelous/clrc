package com.belous.v.clrc.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@Entity
public class Item implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String ip;
    private int port;

    @TypeConverters({ItemConverter.class})
    private Map<String, String> params;

    @Ignore
    public Item(String name, String ip, int port, Map<String, String> params) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.params = params;
    }

    public Item(long id, String name, String ip, int port, Map<String, String> params) {
        this(name, ip, port, params);
        this.id = id;
    }

    public static Item build(Map<String, String> params) {
        String location = params.get("Location");

        String ip = location.substring(location.indexOf("://") + 3, location.lastIndexOf(":"));
        int port = Integer.parseInt(location.substring(location.lastIndexOf(":") + 1));
        String name = params.get("model") + " (" + ip + ")";
        params.remove("name");
        params.remove("support");
        params.remove("id");
        params.remove("Location");
        params.put("active_mode", "0");
        params.put("online", "1");
        return new Item(name, ip, port, params);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return !params.get("online").isEmpty();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isPower() {
        return params.get(ItemEnum.POWER).matches("on|1");
    }

    public int getBright() {
        return Integer.parseInt(isActive_mode() ? params.get(ItemEnum.NL_BR) : params.get(ItemEnum.BRIGHT));
    }

    public String getCt() {
        return params.get(ItemEnum.CT);
    }

    public boolean isActive_mode() {
        return params.get(ItemEnum.ACTIVE_MODE).equals("1");
    }

    public int getColor_mode() {
        return Integer.parseInt(params.get(ItemEnum.COLOR_MODE));
    }

    public String getRgb() {
        return params.get(ItemEnum.RGB);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOnline(boolean online) {
        if (online) {
            params.put("online", "1");
        }
    }

    public void setParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ip.equals(((Item) (obj)).getIp());
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", params=" + params +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(ip);
        parcel.writeInt(port);
        parcel.writeInt(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            long id = in.readLong();
            String name = in.readString();
            String ip = in.readString();
            int port = in.readInt();
            int paramSize = in.readInt();
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < paramSize; i++) {
                params.put(in.readString(), in.readString());
            }
            return new Item(id, name, ip, port, params);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
