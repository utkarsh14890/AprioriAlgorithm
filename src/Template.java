import java.util.ArrayList;
import java.util.HashMap;

public class Template{
	HashMap<String,ArrayList<Double>> map = new HashMap<String,ArrayList<Double>>();
	public int templateFlag = -1;
	public int primaryFlag = -1;
	
	public int num = -1;
	
	public void setTemplateFlag(int i){
		templateFlag = i;
	}
	public void setPrimaryFlag(int i){
		primaryFlag = i;
	}
	public void setNum(int i){
		num = i;
	}
	public void generateRules(){
		System.out.println("Abstract");
	}
}