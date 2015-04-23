import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class TypeAheadService {
    private static final String ADD_COMMAND = "ADD";
    private static final String QUERY_COMMAND = "QUERY";
    private static final String WQUERY_COMMAND = "WQUERY";
    private static final String DEL_COMMAND = "DEL";
    
    private static TypeAheadService instance;
    private static List<Item> itemList;
    private static Trie trie;
    private final static Comparator<Item> itemComparatorByScore = new Comparator<Item>() {
    	@Override
        public int compare(Item item1, Item item2) {
    		float score1 = item1.getScore();
    		float score2 = item2.getScore();
    		if(score1==score2)
    			return item1.getIndex()-item2.getIndex();
    		else if(score1>score2)
    			return 1;
    		else 
    			return -1;
        }
	};
	
    private TypeAheadService()
    {
        itemList = new ArrayList<Item>();
        trie = new Trie();
    }
    public static TypeAheadService getService()
    {
        if(instance==null)
        {
            instance = new TypeAheadService();
        }
        return instance;
    }
    public static void executeCommand(String command)
    {
    	command = command.trim();
        if(command==null || command.isEmpty())
            return;
        if(command.startsWith(ADD_COMMAND))
        {
            addItem(command);
        }
        else if(command.startsWith(QUERY_COMMAND))
        {
        	parseAndExecuteQuery(command);
        }
        else if(command.startsWith(WQUERY_COMMAND))
        {
        	parseAndExecuteWeightedQuery(command);
        }
    }
    private static void parseAndDelete(String command)
    {
    	String[] parameters = command.split(" ");
    	if(parameters.length!=2)
    		return;
    	delete(parameters[2].trim().toLowerCase());
    }
    private static void delete(String id)
    {
    	if(id==null || id.isEmpty())
    		return;
    	
    }
    private static void parseAndExecuteWeightedQuery(String queryString)
    {
    	String[] parameters = queryString.split(" ");
    	if(parameters.length<4)
    		return;
    	int k = Integer.parseInt(parameters[1]);
    	int numberOfBoosts = Integer.parseInt(parameters[2]);
    	ItemComparator comparator = new ItemComparator(numberOfBoosts);
    	for(int i=0;i<numberOfBoosts;i++)
    	{
    		String parameter = parameters[3+i];
    		String[] values = parameter.split(":");
    		comparator.addBooster(values[0], Float.parseFloat(values[1]));
    	}
    	executeQuery(parameters, 3+numberOfBoosts, comparator, k);
    }
    
    private static void parseAndExecuteQuery(String queryString)
    {
    	String[] parameters = queryString.split(" ");
    	if(parameters.length<3)
    		return;
    	int k = Integer.parseInt(parameters[1]);
    	executeQuery(parameters, 2,itemComparatorByScore,k);
    }
    
    private static void executeQuery(String[] queryParameters, int startTermIndex, Comparator comparator, int numberOfItems)
    {
    	if(numberOfItems<=0)
    	{
    		System.out.println();
    		return;
    	}
    	if(startTermIndex>=queryParameters.length)
    		return;
    	Set<Item> intersection = getItemsForQuery(queryParameters[startTermIndex]);
    	for(int i=startTermIndex+1; i<queryParameters.length;i++)
    	{
    		Set<Item> set = getItemsForQuery(queryParameters[i]);
    		intersection.retainAll(set);
    	}
    	sortAndPrintQueryResult(getTopKItems(intersection, numberOfItems, comparator),comparator);
    }
    
    private static void sortAndPrintQueryResult(Set<Item> intersection, Comparator comparator)
    {
    	List<Item> sortedList = new ArrayList<Item>(intersection);
    	Collections.sort(sortedList, comparator);
    	for(int i=sortedList.size()-1;i>=0;i--)
    	{
    		System.out.print(sortedList.get(i).getId()+" ");
    	}
    	System.out.println();
    }
    
    private static Set<Item> getItemsForQuery(String queryString){
    	Set<Item> items = new HashSet<Item>();
    	if(queryString==null || queryString.isEmpty())
    		return items;
    	return trie.traverseTrie(queryString.trim().toLowerCase());
    }
    
    private static Set<Item> getTopKItems(Set<Item> items, int numOfResults, Comparator comparator)
    {
        if(items.size()>numOfResults)
        {
            PriorityQueue<Item> minPriorityQueueByScore = new PriorityQueue<Item>(numOfResults, comparator);
            Item minItem = null;
            for(Item item : items)
            {
            	if(minPriorityQueueByScore.size()<numOfResults)
            	{
            		minPriorityQueueByScore.add(item);
            		minItem = minPriorityQueueByScore.peek();
            	}
            	else
            	{
            		if(comparator.compare(item, minItem)==1)
            		{
            			minPriorityQueueByScore.poll();
            			minPriorityQueueByScore.add(item);
            			minItem = minPriorityQueueByScore.peek();
            		}
            	}
            }
            return new HashSet<Item>(minPriorityQueueByScore);
        }
        else
        {
        	return items;
        }
    }
    private static void addItem(String itemString)
    {
        Item item = parseAddCommand(itemString, itemList.size());
        itemList.add(item);
        //trie.printTrie();
    }
    private static Item parseAddCommand(String itemString, int index)
    {
        String[] parameters = itemString.split(" ");
        if(parameters.length<5)
            throw new IllegalArgumentException("Insufficient data to ADD: "+itemString);
        StringBuffer valueBuffer = new StringBuffer();
        String value = parameters[4].trim();
        valueBuffer.append(value);
        for(int k=5;k<parameters.length;k++)
        {
            valueBuffer.append(" ");
            valueBuffer.append(parameters[k]);
        }
        Item item = new Item(parameters[1], index, parameters[2], Float.parseFloat(parameters[3]), valueBuffer.toString());
        for(int i=4;i<parameters.length-1;i++)
        {
            trie.addWord(parameters[i].trim().toLowerCase(), item);
        }
        String lastWord = parameters[parameters.length-1];
        if(item.getType().equals(Item.ItemType.QUESTION))
        {
            lastWord = lastWord.trim().substring(0, lastWord.length()-1);
        }
        trie.addWord(lastWord.trim().toLowerCase(), item);
        return item;
    }
}
