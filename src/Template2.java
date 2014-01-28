import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Template2 extends Template{
	public void generateRules(){
		if(primaryFlag==0)
			generateSizeRule(Apriori.keyHead);
		else if(primaryFlag==1)
			generateSizeBody(Apriori.keyBody);
		else if(primaryFlag==2)
			generateSizeHead(Apriori.keyHead);
	}
	
	public void generateSizeHead(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		for(ArrayList<String> s : temp.keySet()){
			Iterator<ArrayList<String>> it = temp.get(s).iterator();
			while(it.hasNext()){
				ArrayList<String> l = it.next();
				if(s.size()>=num){
					String part1 = Arrays.toString(s.toArray());
					String part2 = Arrays.toString(l.toArray());
					String rule = part1+"-->"+part2;
					ArrayList<Double> dl = Apriori.keyRule.get(rule);
					map.put(rule, dl);
				}
			}
		}
	}
	public void generateSizeBody(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		for(ArrayList<String> s : temp.keySet()){
			Iterator<ArrayList<String>> it = temp.get(s).iterator();
			while(it.hasNext()){
				ArrayList<String> l = it.next();
				if(s.size()>=num){
					String part1 = Arrays.toString(s.toArray());
					String part2 = Arrays.toString(l.toArray());
					String rule = part2+"-->"+part1;
					ArrayList<Double> dl = Apriori.keyRule.get(rule);
					map.put(rule, dl);
				}
			}
		}
	}
	public void generateSizeRule(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		for(ArrayList<String> s : temp.keySet()){
			Iterator<ArrayList<String>> it = temp.get(s).iterator();
			while(it.hasNext()){
				ArrayList<String> l = it.next();
				if(s.size()+l.size()>=num){
					String part1 = Arrays.toString(s.toArray());
					String part2 = Arrays.toString(l.toArray());
					String rule = part1+"-->"+part2;
					ArrayList<Double> dl = Apriori.keyRule.get(rule);
					map.put(rule, dl);
				}
			}
		}
	}
}