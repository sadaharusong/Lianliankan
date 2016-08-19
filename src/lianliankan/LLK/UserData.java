package lianliankan.LLK;

public class UserData {
	private String name[]= new String[5];
	private String date[]= new String[5];
	private String hiScore[]= new String[5];
	public String getName(int i) {
		return name[i];
	}
	public void setName(String name,int i) {
		this.name[i] = name;
	}
	public String getDate(int i) {
		return date[i];
	}
	public void setDate(String date,int i) {
		this.date[i] = date;
	}
	public String getHiScore(int i) {
		return hiScore[i];
	}
	public void setHiScore(String hiScore,int i) {
		this.hiScore[i] = hiScore;
	}
	
	public String toString(int i) {
		return "UserData [date=" + date[i] + ", hiScore=" + hiScore[i] + ", name="
				+ name[i] + "]";
	}
	
	
	
}
