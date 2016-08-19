package lianliankan.LLK;

import java.io.*;

import android.widget.TextView;


public class Configuration {
	File file = new File("/sdcard/llk.dat");
	public Configuration(){
		load();	
	}
	
	public void save(UserData data,int i){
		try {
			PrintWriter out = null;
			try {
				out = new PrintWriter(file,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*PrintStream out = 
				new PrintStream(
					new FileOutputStream(file));*/
			
				out.println(data.getName(i));
				out.println(data.getDate(i));
				out.println(data.getHiScore(i));
			
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}	
	}
	
	public UserData load(){
		UserData data = null;
		try {
			data = new UserData();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			
				data.setName(in.readLine(),0);
				data.setDate(in.readLine(),0);
				data.setHiScore(in.readLine(),0);
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return data;	
	}
	
	public String readFile(InputStream inputStream){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String str = null;
			while((str=in.readLine()) != null){
				sb.append(str+"\n");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sb.toString();
	}
}
