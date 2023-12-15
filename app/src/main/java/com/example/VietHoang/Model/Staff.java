package .Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Staff implements Parcelable {
    private Integer staff_ID, staff_Time;
    private String staff_Name, staff_SDT, staff_birth, staff_Calender, staff_Password, staff_Role;
    private Double staff_Money;
    private byte[] staff_image;

    public Staff() {
    }

    public Staff(String staff_Name, String staff_Role, byte[] staff_image) {
        this.staff_Name = staff_Name;
        this.staff_Role = staff_Role;
        this.staff_image = staff_image;
    }

    public Staff(Integer staff_Time, String staff_Name, String staff_SDT, String staff_birth, String staff_Calender, String staff_Role, Double staff_Money, byte[] staff_image) {
        this.staff_Time = staff_Time;
        this.staff_Name = staff_Name;
        this.staff_SDT = staff_SDT;
        this.staff_birth = staff_birth;
        this.staff_Calender = staff_Calender;
        this.staff_Role = staff_Role;
        this.staff_Money = staff_Money;
        this.staff_image = staff_image;
    }

    public Staff(Integer staff_ID, Integer staff_Time, String staff_Name, String staff_SDT, String staff_birth, String staff_Calender, String staff_Role, Double staff_Money, byte[] staff_image) {
        this.staff_ID = staff_ID;
        this.staff_Time = staff_Time;
        this.staff_Name = staff_Name;
        this.staff_SDT = staff_SDT;
        this.staff_birth = staff_birth;
        this.staff_Calender = staff_Calender;
        this.staff_Role = staff_Role;
        this.staff_Money = staff_Money;
        this.staff_image = staff_image;
    }

    public Staff(Integer staff_ID, Integer staff_Time, String staff_Role, String staff_Name, String staff_SDT, String staff_birth, String staff_Calender, String staff_Password, Double staff_Money, byte[] staff_image) {
        this.staff_ID = staff_ID;
        this.staff_Time = staff_Time;
        this.staff_Role = staff_Role;
        this.staff_Name = staff_Name;
        this.staff_SDT = staff_SDT;
        this.staff_birth = staff_birth;
        this.staff_Calender = staff_Calender;
        this.staff_Password = staff_Password;
        this.staff_Money = staff_Money;
        this.staff_image = staff_image;
    }

    protected Staff(Parcel in) {
        if (in.readByte() == 0) {
            staff_ID = null;
        } else {
            staff_ID = in.readInt();
        }
        if (in.readByte() == 0) {
            staff_Time = null;
        } else {
            staff_Time = in.readInt();
        }
        staff_Name = in.readString();
        staff_SDT = in.readString();
        staff_birth = in.readString();
        staff_Calender = in.readString();
        staff_Password = in.readString();
        staff_Role = in.readString();
        if (in.readByte() == 0) {
            staff_Money = null;
        } else {
            staff_Money = in.readDouble();
        }
        staff_image = in.createByteArray();
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    public Integer getStaff_ID() {
        return staff_ID;
    }

    public void setStaff_ID(Integer staff_ID) {
        this.staff_ID = staff_ID;
    }

    public Integer getStaff_Time() {
        return staff_Time;
    }

    public void setStaff_Time(Integer staff_Time) {
        this.staff_Time = staff_Time;
    }

    public String getStaff_Role() {
        return staff_Role;
    }

    public void setStaff_Role(String staff_Role) {
        this.staff_Role = staff_Role;
    }

    public String getStaff_Name() {
        return staff_Name;
    }

    public void setStaff_Name(String staff_Name) {
        this.staff_Name = staff_Name;
    }

    public String getStaff_SDT() {
        return staff_SDT;
    }

    public void setStaff_SDT(String staff_SDT) {
        this.staff_SDT = staff_SDT;
    }

    public String getStaff_birth() {
        return staff_birth;
    }

    public void setStaff_birth(String staff_birth) {
        this.staff_birth = staff_birth;
    }

    public String getStaff_Calender() {
        return staff_Calender;
    }

    public void setStaff_Calender(String staff_Calender) {
        this.staff_Calender = staff_Calender;
    }

    public String getStaff_Password() {
        return staff_Password;
    }

    public void setStaff_Password(String staff_Password) {
        this.staff_Password = staff_Password;
    }

    public Double getStaff_Money() {
        return staff_Money;
    }

    public void setStaff_Money(Double staff_Money) {
        this.staff_Money = staff_Money;
    }

    public byte[] getStaff_image() {
        return staff_image;
    }

    public void setStaff_image(byte[] staff_image) {
        this.staff_image = staff_image;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (staff_ID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(staff_ID);
        }
        if (staff_Time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(staff_Time);
        }
        dest.writeString(staff_Name);
        dest.writeString(staff_SDT);
        dest.writeString(staff_birth);
        dest.writeString(staff_Calender);
        dest.writeString(staff_Password);
        dest.writeString(staff_Role);
        if (staff_Money == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(staff_Money);
        }
        dest.writeByteArray(staff_image);
    }
}
