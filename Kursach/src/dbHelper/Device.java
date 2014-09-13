package dbHelper;

import android.R.bool;


public class Device {
	 String name,telphone;
	 Boolean connect;
	 
	 public Device(String name,String telphone,Boolean connect){
		 this.name=name;
		 this.telphone=telphone;
		 this.connect=connect;	 
	 }
	 public String getName(){
		 return name;
	 }
	 public String getTelphone(){
		 return telphone;
	 }
	 public Boolean getConnect(){
		 return connect;
	 }
}
