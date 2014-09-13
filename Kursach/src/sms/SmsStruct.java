package sms;

import java.util.ArrayList;

import android.util.Log;


public class SmsStruct {
	ArrayList<Cordinat> cordinats = null;
	String message = null;

	public SmsStruct(String message) {
		this.message = message;

	}

	public SmsStruct(ArrayList<Cordinat> cordinats) {
		this.cordinats = cordinats;
	}

	public String getSMS() {
		if (message == null) {
			message = "location:\n";
			for (int i = 0; i < cordinats.size(); i++)
				message += cordinats.get(i).latitude + " "
						+ cordinats.get(i).longitude + "\n";
		}
		return message;
	}

	public ArrayList<Cordinat> getCordinats() {
		if (cordinats == null) {
			cordinats=new ArrayList<Cordinat>();
			String s = message.substring(9);
			ArrayList<Integer> space = new ArrayList<Integer>();
			ArrayList<Integer> enter = new ArrayList<Integer>();
			for(int i=0;i<s.length();i++)
			{
				String ss=s.substring(i,i+1);
				if (ss.equals("\n")) 
					enter.add(i);
				if (ss.equals(" ")) 
					space.add(i);				
			}
			Log.d("log", enter.size()+" "+space.size());
			for(int i=0;i<space.size();i++)
			{
			
				Double latitude=Double.valueOf(s.substring(enter.get(i),space.get(i)));	
				Double longitude=Double.valueOf(s.substring(space.get(i)+1,enter.get(i+1)));	
				Cordinat c=new Cordinat(latitude, longitude);
				cordinats.add(c);
			}
		}
		return this.cordinats;

	}


}
