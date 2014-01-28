import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Template1 extends Template{
	
	public int secondaryFlag  = -1;
	public ArrayList<String> itemSet = new ArrayList<String>();
	public void setSecondaryFlag(int i){
		secondaryFlag = i;
	}
	public void generateRules(){
		if(primaryFlag == 0){
			//temp2 = Apriori.keyRule;
			if(secondaryFlag == 0)
				generateAnyRule(Apriori.keyRule);
			else if(secondaryFlag == 1)
				generateNumberRule(Apriori.keyRule);
			else if(secondaryFlag == 2)
				generateNoneRule(Apriori.keyRule);
			else
				System.out.println("Error");
		}
		else if(primaryFlag == 1){
			if(secondaryFlag == 0)
				generateAnyBody(Apriori.keyBody);
			else if(secondaryFlag == 1)
				generateNumberBody(Apriori.keyBody);
			else if(secondaryFlag == 2)
				generateNoneBody(Apriori.keyBody);
			else
				System.out.println("Error");
		}
		else if(primaryFlag == 2){
			if(secondaryFlag == 0)
				generateAnyHead(Apriori.keyHead);
			else if(secondaryFlag == 1)
				generateNumberHead(Apriori.keyHead);
			else if(secondaryFlag == 2)
				generateNoneHead(Apriori.keyHead);
			else
				System.out.println("Error");
		}
	}
	public void generateAnyHead(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		Iterator<String> it = itemSet.iterator();
		while(it.hasNext()){
			String item = it.next();
			for(ArrayList<String> s:temp.keySet()){
				if(s.contains(item)){
					Iterator<ArrayList<String>> it1 = temp.get(s).iterator();
					while(it1.hasNext()){
						String part2 = Arrays.toString(it1.next().toArray());
						String part1 = Arrays.toString(s.toArray()); 
						String rule = part1+"-->"+part2;
						if(!map.containsKey(rule)){
							//String finalRule = part1+"-->"+part2;
							ArrayList<Double> list = Apriori.keyRule.get(rule);
							map.put(rule, list);
						}
					}
				}
			}
		}
//		if(map.isEmpty()){
//			System.out.println("There are no  appropriate results.");
//		}
//		else{
//			System.out.println("The rules are:");
//			for(String s:map.values()){
//				System.out.println(s);
//			}
//		}	
	}
	public void generateAnyBody(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		Iterator<String> it = itemSet.iterator();
		while(it.hasNext()){
			String item = it.next();
			for(ArrayList<String> s:temp.keySet()){
				if(s.contains(item)){
					Iterator<ArrayList<String>> it1 = temp.get(s).iterator();
					while(it1.hasNext()){
						String part2 = Arrays.toString(it1.next().toArray());
						String part1 = Arrays.toString(s.toArray()); 
						String rule = part2+"-->"+part1;
						if(!map.containsKey(rule)){
							//String finalRule = part2+"-->"+part1;
							ArrayList<Double> list = Apriori.keyRule.get(rule);
							map.put(rule, list);
						}
					}
				}
			}
		}
//		if(map.isEmpty()){
//			System.out.println("There are no  appropriate results.");
//		}
//		else{
//			System.out.println("The rules are:");
//			for(String s:map.values()){
//				System.out.println(s);
//			}
//		}
	}
	
	public void generateAnyRule(Map<String,ArrayList<Double>> temp){
		Iterator<String> it = itemSet.iterator();
		while(it.hasNext()){
			String item = it.next();
			for(String s:temp.keySet()){
				if(s.contains(item)){
					if(!map.containsKey(s)){
						ArrayList<Double> list = temp.get(s);
						map.put(s, list);
					}
				}
			}
		}
	}
	
	public void generateNoneHead(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		Iterator<String> it = itemSet.iterator();
		while(it.hasNext()){
			String item = it.next();
			for(ArrayList<String> s:temp.keySet()){
				if(!s.contains(item)){
					Iterator<ArrayList<String>> it1 = temp.get(s).iterator();
					while(it1.hasNext()){
						String part2 = Arrays.toString(it1.next().toArray());
						String part1 = Arrays.toString(s.toArray()); 
						String rule = part1+"-->"+part2;
						if(!map.containsKey(rule)){
							//String finalRule = part1+"-->"+part2;
							ArrayList<Double> list = Apriori.keyRule.get(rule);
							map.put(rule, list);
						}
					}
				}
			}
		}
	}
	
	public void generateNoneBody(Map<ArrayList<String>,ArrayList<ArrayList<String>>> temp){
		Iterator<String> it = itemSet.iterator();
		while(it.hasNext()){
			String item = it.next();
			for(ArrayList<String> s:temp.keySet()){
				if(!s.contains(item)){
					Iterator<ArrayList<String>> it1 = temp.get(s).iterator();
					while(it1.hasNext()){
						String part2 = Arrays.toString(it1.next().toArray());
						String part1 = Arrays.toString(s.toArray()); 
						String rule = part2+"-->"+part1;
						if(!map.containsKey(rule)){
							//String finalRule = part2+"-->"+part1;
							ArrayList<Double> list = Apriori.keyRule.get(rule);
							map.put(rule, list);
						}
					}
				}
			}
		}
	}
	
	public void generateNoneRule(Map<String,ArrayList<Double>> temp){
		Iterator<String> it = itemSet.iterator();
		while(it.hasNext()){
			String item = it.next();
			for(String s:temp.keySet()){
				if(!s.contains(item)){
					if(!map.containsKey(s)){
						ArrayList<Double> list = temp.get(s);
						map.put(s, list);
					}
				}
			}
		}
	}
	
	public void generateNumberHead(Map<ArrayList<String>,ArrayList<ArrayList<String>>> KeyHead)
	{
		Map<String,Integer> tempRule = new HashMap<String,Integer>();
		for(String it: itemSet)
		{
			//System.out.println("Item	:"+it);
			for (ArrayList<String> entryHead : KeyHead.keySet()) {
				if(entryHead.contains(it)){
					for(ArrayList<String> impliedBody: KeyHead.get(entryHead))
					{
						//Form rules as Strings
						String rule = entryHead + "-->" + impliedBody;
						if(tempRule.containsKey(rule))
						{
							int counter = tempRule.get(rule);
							counter ++;
							tempRule.put(rule, counter);
						}
						else
						{
							tempRule.put(rule, 1);
						}
					}
				}
			}
		}
		for(String s:tempRule.keySet()){
			if(tempRule.get(s)>num){
				ArrayList<Double> list = Apriori.keyRule.get(s);
				map.put(s, list);
			}
		}
	}
	public void generateNumberBody(Map<ArrayList<String>,ArrayList<ArrayList<String>>> KeyBody)
	{
		Map<String,Integer> tempRule = new HashMap<String,Integer>();
		for(String it: itemSet)
		{	
			//System.out.println("Item	:"+it);
			for (ArrayList<String> entryBody : KeyBody.keySet()) {
				if(entryBody.contains(it)){
					for(ArrayList<String> impliedHead: KeyBody.get(entryBody))
					{
						//Form rules as Strings
						String rule = impliedHead + "-->" + entryBody;
						if(tempRule.containsKey(rule))
						{
							int counter = tempRule.get(rule);
							counter ++;
							tempRule.put(rule, counter);
						}
						else
						{
							tempRule.put(rule, 1);
						}
					}
				}
			}
		}
		for(String s:tempRule.keySet()){
			if(tempRule.get(s)>num){
				ArrayList<Double> list = Apriori.keyRule.get(s);
				map.put(s, list);
			}
		}
	}
	
	public void generateNumberRule(Map<String,ArrayList<Double>> KeyRule)
	{
		Map<String,Integer> tempRule = new HashMap<String,Integer>();

		for(String it: itemSet)
		{	
			//System.out.println("Item	:"+it);
			for (String entryRule : KeyRule.keySet()) {
				if(entryRule.contains(it)){
					if(tempRule.containsKey(entryRule))
					{
						int counter = tempRule.get(entryRule);
						counter ++;
						tempRule.put(entryRule, counter);
					}
					else
					{
						tempRule.put(entryRule, 1);
					}
				}

			}
		}

		for(String s:tempRule.keySet()){
			if(tempRule.get(s)>num){
				ArrayList<Double> list = Apriori.keyRule.get(s);
				map.put(s, list);
			}
		}
	}
}