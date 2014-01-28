import java.io.*;
import java.util.*;

public class Apriori {

	static Vector<String> candidates = new Vector<String>();
	static int numberOfSamples = 100;
	static int numberOfItems = 101;
	static int numberOfItemsUsed = 204;
	static double minsup;
	static double minconfidence = 0.70;
	static String[] oneVal;
	static String dataFilePath = "cleansedDataSet.txt";
	
	static ArrayList<String> input_line = new ArrayList<String>();
	static Vector<String> totalFrequentCandidates = new Vector<String>();
	static HashSet<HashSet<String>> powerSet = new HashSet<HashSet<String>>();
	static ArrayList<ArrayList<String>> data_List = new ArrayList<ArrayList<String>>();
	static Map<String,Integer> indexMap = new HashMap<String,Integer>();
	static Map<HashSet<String>,Integer> supportMap = new HashMap<HashSet<String>,Integer>();
	
	// HashMaps to store rules
	static Map<ArrayList<String>,ArrayList<ArrayList<String>>> keyHead = new HashMap<ArrayList<String>,ArrayList<ArrayList<String>>>();
	static Map<ArrayList<String>,ArrayList<ArrayList<String>>> keyBody = new HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>();
	static Map<String,ArrayList<Double>> keyRule = new HashMap<String,ArrayList<Double>>();
	static PrintWriter out ;
	public static ArrayList<Template> objList = new ArrayList<Template>();
	public static ArrayList<Integer> flag = new ArrayList<Integer>();
	
	public static void log(String message) throws IOException
	{
		PrintWriter out = new PrintWriter(new FileWriter("Output.txt"),true);
		out.append(message);
		out.close();
	}
	
	public static void converLineToList(String input)
	{
		String item;
		StringTokenizer tk;
		tk = new StringTokenizer(input);
		while(tk.hasMoreElements())
		{
			item = tk.nextToken();
			if(item.compareToIgnoreCase("UP")==0)
			{
				input_line.add("UP");
				input_line.add("0");
			}
			else if(item.compareToIgnoreCase("Down")==0)
			{
				input_line.add("0");
				input_line.add("Down");
			}
			if(item.compareToIgnoreCase("ALL")==0)
			{
				input_line.add("ALL");
				input_line.add("0");
				input_line.add("0");
				input_line.add("0");
				
			}
			else if(item.compareToIgnoreCase("Breast")==0)
			{
				input_line.add("0");
				input_line.add("Breast_Cancer");
				input_line.add("0");
				input_line.add("0");
			}
			else if(item.compareToIgnoreCase("Colon")==0)
			{
				input_line.add("0");
				input_line.add("0");
				input_line.add("Colon_Cancer");
				input_line.add("0");
			}
			else if(item.compareToIgnoreCase("AML")==0)
			{
				input_line.add("0");
				input_line.add("0");
				input_line.add("0");
				input_line.add("AML");
			}
				
		}
	}

	public static void storeDataInList(ArrayList<String> input)
	{
		ArrayList<String> entryName = new ArrayList<String>();
		entryName.addAll(input);
		data_List.add(entryName);
	}
	// Function to generate candidate itemsets
	public static void generateCandidates(int itemSetNumber)
	{ 
		//System.out.println("---------- Generating Candidate Itemsets  --------------");
		Vector<String> tempCandidates = new Vector<String>();
		tempCandidates.ensureCapacity(204);
		String str1,str2;
		StringTokenizer st1,st2;
		
		
		if(itemSetNumber == 1) 
		{
			// For first set, candidates are just numbers
			for(int i =1;i<=numberOfItems-1;i=i+1)
			{
				tempCandidates.add("g"+Integer.toString(i)+"up");
				tempCandidates.add("g"+Integer.toString(i)+"down");
			}
			tempCandidates.add("ALL");
			tempCandidates.add("Breast_Cancer");
			tempCandidates.add("Colon_Cancer");
			tempCandidates.add("AML");
		}
		else if(itemSetNumber == 2) // Second itemset is combination of all itemset 1
		{
			// Form combination from previous frequent itemsets
			for(int i=0;i<candidates.size();i++)
			{
				st1 = new StringTokenizer(candidates.get(i));
				str1 = st1.nextToken();
				for( int j=i+1; j<candidates.size();j++)
				{
					st2 = new StringTokenizer(candidates.elementAt(j));
					str2 = st2.nextToken();
					tempCandidates.add(str1+" "+str2);
				}
			}
		}
		else
		{
			for(int i=0;i<candidates.size();i++)
			{
				for(int j=i+1;j<candidates.size();j++)
				{
					str1 = new String();
				    str2 = new String();
				    st1 = new StringTokenizer(candidates.get(i));
				    st2 = new StringTokenizer(candidates.get(j));
				
				    // Check to see if the First n-2 tokens are same
				    for(int n=0;n<itemSetNumber-2;n++)
				    {
				    	str1 = str1 + " " + st1.nextToken();
				    	str2 = str2 + " " + st2.nextToken();
				    }
				    if(str2.compareToIgnoreCase(str1)==0)
				    {
				    	tempCandidates.add((str1 + " " + st1.nextToken() + " " + st2.nextToken()).trim());
				    }
				
				}
			}
			
		}
		// Copy to candidates
		candidates.clear();
		candidates = new Vector<String>(tempCandidates);
		tempCandidates.clear();

	}

	// Function to generate frequent Itemsets
	@SuppressWarnings("resource")
	public static void generateFrequentItemSets(int itemSetNumber)
	{
		//System.out.println("---------- Generating Frequent Itemsets  --------------");
		
		Vector<String> frequentCandidates = new Vector<String>();
		Vector<Integer> minsupportForFrequentItemsets = new Vector<Integer>();
		String frequentItemset = null;
		boolean sample[] = new boolean[numberOfItemsUsed];
	    int count[] = new int [candidates.size()];
        StringTokenizer st,stFile,fToken;
        String temp= null,temptoken = null;
        boolean match;
        
        
        try {
        	// foreach sample
        	for(int i=0;i<data_List.size();i++)
        	{
        		//Read one sample from data_list
        		ArrayList<String> extractInput = new ArrayList<String>();
        		extractInput.addAll(data_List.get(i));

        		for(int j=0;j<extractInput.size();j++)
        		{
        			temptoken = extractInput.get(j);
        			sample[j]= (temptoken.compareToIgnoreCase(oneVal[j])==0);
        		}
        		String temptoken1=null;
        		for(int c=0;c<candidates.size();c++)
        		{
        			match = false;
        			st = new StringTokenizer(candidates.get(c));
        			while(st.hasMoreTokens())
        			{
        				temptoken1 = st.nextToken();
        				match = sample[indexMap.get(temptoken1)];

        				if(!match)
        					break;
        			}	
        			if(match)
        			{
        				count[c]++;
        			}	

        		}
        	}

        } 
        catch (Exception e1) 
        {
        	// TODO Auto-generated catch block
        	e1.printStackTrace();
        }

        // Pick up frequent Itemsets for a given minsup
        for(int i=0;i<candidates.size();i++)
        {
        	if(count[i]/(double)numberOfSamples>= minsup)
        	{
        		frequentItemset = candidates.get(i);
        		frequentCandidates.add(frequentItemset);
        		minsupportForFrequentItemsets.add(count[i]);
        		totalFrequentCandidates.add(frequentItemset);
        		storeSupportforFreqitemsets(frequentItemset,count[i]);

        	}

        }

        // Copy frequentCandidates to candidates
        candidates.clear();
        candidates = new Vector<String>(frequentCandidates);
        frequentCandidates.clear();
	}
	

	public static void storeSupportforFreqitemsets(String frequentItemset, int minsupport)
	{
		//Store frequentItemset as a Vector of Strings
		StringTokenizer tk = new StringTokenizer(frequentItemset);
		List<String> tempfrequentItemset = new ArrayList<String>();
		HashSet<String> tempSet = new HashSet<String>();
		while(tk.hasMoreElements())
		{
			tempSet.add(tk.nextToken()); 
		}
		
		if(! supportMap.containsKey(frequentItemset))
		{
			supportMap.put(tempSet, minsupport);
		}
	}
		
	public static void printset(HashSet<String> testSet)
	{
		for (String s :testSet)
		{
			System.out.println(s.toString());
		}
		
	}
	
	public static void createSet(Vector<String> input)
	{
		StringTokenizer tk;
		
		for(String s:input)
		{
			HashSet<String> tempSet = new HashSet<String>();
			tk = new StringTokenizer(s);
			while(tk.hasMoreElements())
			{
				tempSet.add(tk.nextToken());
			}
			generatePowerset(tempSet,tempSet.size()); //Function to Generate Power Set of a Set.
		}	
	}
	
	public static void generatePowerset(HashSet<String> set, int size)
	{
		
		int powerSetSize = (int) Math.pow(2,size);
		Vector<String> indexList = new Vector<String>(set); //Convert set to a ArrayList
		
		for(int counter=1; counter < powerSetSize-1;counter++)
		{
			HashSet<String> tempSet2 = new HashSet<String>();
			for(int j =0;j<size;j++)
			{
				if((counter & (1<<j))>0)
				{
					//pick up set[j]
					tempSet2.add(indexList.elementAt(j)); //Index the ArrayList
					powerSet.add(tempSet2);
				}
				
			}
		}
		
		// Generate Association Rule
		generateAssociationRules(set,powerSet);
		for( HashSet<String> s: powerSet)
		{	
			//System.out.println(s);
		}
		
		powerSet.clear();
		
	}
	
	public static void generateAssociationRules(HashSet<String> set,HashSet<HashSet<String>> powerset)
	{
		//Store association rules for minsup=0.5 in a table
		ArrayList<String> l = new ArrayList<String>(set);
		ArrayList<String> ltemp = new ArrayList<String>();
		ltemp.addAll(l);
		
		for( HashSet<String> individualSet: powerset)
		{
			ArrayList<String> s = new ArrayList<String>(individualSet);
			
			// Form Rules
			for(String str:s)
			{
				int index =ltemp.indexOf(str);
				ltemp.remove(index);
			}
			// Now store S->(L-S) in a HashMap
			storeAssociationRules(s,ltemp);          /* Call to store Association Rules */
			ltemp.clear();
			ltemp.addAll(l);	
		}
				
	}
	
	public static void storeAssociationRules(ArrayList<String> s,ArrayList<String> lminus)
	{
		/* HashMap with Key = Head */
		populateKeyHead(s,lminus);
			
		/* HashMap with Key = Body */
		populateKeyBody(s,lminus);
		
		/* HashMap with Key = Rule */
		populateKeyRule(s, lminus);
	}
	
	public static void populateKeyHead(ArrayList<String> s,ArrayList<String> lminus)
	{
		if(!keyHead.containsKey(s))
		{
			ArrayList<ArrayList<String>> newEntry = new ArrayList<ArrayList<String>>();
			ArrayList<String> newElem = new ArrayList<String>();
			newElem.addAll(lminus);
			newEntry.add(newElem);
			ArrayList<String> keyElem = new ArrayList<String>();
			keyElem.addAll(s);
			keyHead.put(keyElem, newEntry);
		}		
		else if(keyHead.containsKey(s))
		{
			ArrayList<ArrayList<String>> extractEntry = new ArrayList<ArrayList<String>>();
			extractEntry = keyHead.get(s);
			ArrayList<String> addElem = new ArrayList<String>();
			addElem.addAll(lminus);
			extractEntry.add(addElem);
			keyHead.put(s, extractEntry);
		}
	}
	
	public static void populateKeyBody(ArrayList<String> s,ArrayList<String> lminus)
	{
		if(!keyBody.containsKey(lminus))
		{
			ArrayList<ArrayList<String>> newEntry2 = new ArrayList<ArrayList<String>>();
			ArrayList<String> newElem2 = new ArrayList<String>();
			newElem2.addAll(s);
			newEntry2.add(newElem2);
			ArrayList<String> keyElem = new ArrayList<String>();
			keyElem.addAll(lminus);
			keyBody.put(keyElem, newEntry2);
		}
		else if(keyBody.containsKey(lminus))
		{
			ArrayList<ArrayList<String>> extractEntry2 = new ArrayList<ArrayList<String>>();
			extractEntry2 = keyBody.get(lminus);
			ArrayList<String> addNew2 = new ArrayList<String>();
			addNew2.addAll(s);
			extractEntry2.add(addNew2);
			keyBody.put(lminus, extractEntry2);
		}
	}
	
	public static void populateKeyRule(ArrayList<String> s,ArrayList<String> lminus)
	{
		String str = Arrays.toString(s.toArray()) + "-->" + Arrays.toString(lminus.toArray());
		ArrayList<Double> retVal = calculateSupport(s, lminus);
		ArrayList<Double> sup = new ArrayList<Double>();
		sup.addAll(retVal);
		keyRule.put(str, sup);
	}
	
	public static ArrayList<Double> calculateSupport(ArrayList<String> s,ArrayList<String> lminus)
	{
		int supportForS,supportForL;
		HashSet<String> l = new HashSet<String>();
		HashSet<String> S = new HashSet<String>();
		S.addAll(s);
		
		for(String st :s)
			l.add(st);
		for(String st :lminus)
			l.add(st);
		
		supportForL = supportMap.get(l);
		supportForS = supportMap.get(S);
		double confidence =(double) supportForL/supportForS; 
		double support = (double) supportForL/numberOfSamples;
		ArrayList<Double> sup = new ArrayList<Double>();
		sup.add(support);
		sup.add(confidence);
		return sup;
	}
	
	public static void inputTemplate()
	{
		//Parse user input
		flag.clear();
		System.out.println("Enter the input template:");
		Parser parser = new Parser(System.in);
		//Template ob = new Template();
		try {
			objList = Parser.Program();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Template ob : objList){
			ob.generateRules();
		}
		if(objList.size()==1){
			print(objList.get(0).map);
		}
		else if(objList.size()>=2){
			Map<String, ArrayList<Double>> res = new HashMap<String, ArrayList<Double>>();
			if(flag.get(0)==0){
				res = and(objList.get(0).map,objList.get(1).map);
			}
			else if(flag.get(0)==1){
				res = or(objList.get(0).map,objList.get(1).map);
			}
			for(int i=2,j=1;i<objList.size();i++,j++){
				if(flag.get(j)==0){
					res = and(res,objList.get(i).map);
				}
				else if(flag.get(j)==1){
					res = or(res,objList.get(i).map);
				}
			}
			print(res);
		}
	}
	
	public static Map<String, ArrayList<Double>> and(Map<String, ArrayList<Double>> table1,Map<String, ArrayList<Double>> table2)
	{
		Map<String, ArrayList<Double>> resultTable = new HashMap<String, ArrayList<Double>>();
		for(String table1Entry: table1.keySet())
		{
			if(table2.containsKey(table1Entry))
			{
				resultTable.put(table1Entry, table1.get(table1Entry));
			}
		}
		return resultTable;
	}	

	public static Map<String, ArrayList<Double>> or(Map<String, ArrayList<Double>> table1,Map<String, ArrayList<Double>> table2){
		Map<String, ArrayList<Double>> resultTable = new HashMap<String, ArrayList<Double>>();
		resultTable.putAll(table1);
		for(String table2Entry: table2.keySet())
		{
			if(!table1.containsKey(table2Entry))
			{
				resultTable.put(table2Entry, table2.get(table2Entry));
			}
		}
		return resultTable;
	}
	public static void print(Map<String,ArrayList<Double>> map){
		if(map.isEmpty())
			System.out.println("There are no matching rules.");
		else{
			System.out.println("Matching rules are:");
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String rule = it.next();
				ArrayList<Double> list = map.get(rule);
				System.out.println(rule + " support = "+ list.get(0) + " confidence = " + list.get(1));
			}
		}
	}
	public static void readData()
	{
		String input;
		FileInputStream file_in; //file input stream
        BufferedReader data_in; //data input stream//
        try
        {
        	file_in = new FileInputStream(dataFilePath);
        	data_in = new BufferedReader(new InputStreamReader(file_in));

        	for(int i=0;i<numberOfSamples;i++)
        	{
        		input = data_in.readLine();
        		converLineToList(input);
        		storeDataInList(input_line);
        		input_line.clear();
        	}
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	public static void initializeOneVal()
	{
		oneVal = new String[numberOfItemsUsed];
		for(int i=0;i<numberOfItemsUsed-4;i++)
		{
			if(i==0 || i%2 == 0)
			{
				oneVal[i]="UP";
				
			}
			else
			{
				oneVal[i]="Down";
			}
		}
		oneVal[200] = "ALL";
		oneVal[201] = "Breast_Cancer";
		oneVal[202] = "Colon_Cancer";
		oneVal[203]=  "AML";
		
	}
	public static void initializeIndexMap()
	{
		int counter = 0;
		for(int k=1;k<=100;k=k+1)
		{
				indexMap.put("g"+Integer.toString(k)+"up",counter);
				indexMap.put("g"+Integer.toString(k)+"down",counter+1);
				counter = counter+2;
			
		}
		indexMap.put("ALL", 200);
		indexMap.put("Breast_Cancer", 201);
		indexMap.put("Colon_Cancer", 202);
		indexMap.put("AML", 203);
	}
	public static void main(String[] args)
	{
		initializeOneVal();
		initializeIndexMap();
		readData();
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the support");
		minsup = scan.nextDouble();
		
		for(int m=0;m<1;m++) // for 5 values of minimum support
		{	
			int itemSetNumber = 1;
			candidates.clear();	
			//Start Apriori Process
			do{
				generateCandidates(itemSetNumber);
				generateFrequentItemSets(itemSetNumber);
				itemSetNumber ++;
				
			}while(candidates.size()>1);
			
			// Generate PowerSet
			createSet(totalFrequentCandidates);


			System.out.println("Number of Frequent itemsets	:"+totalFrequentCandidates.size());
			totalFrequentCandidates.clear();
		}
		
		// Input Templates and parse it
		inputTemplate();
		
	} // End of Main
}
