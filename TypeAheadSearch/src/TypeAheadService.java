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
    }
    private static void parseAndExecuteQuery(String queryString)
    {
    	String[] parameters = queryString.split(" ");
    	if(parameters.length<3)
    		return;
    	int k = Integer.parseInt(parameters[1]);
    	Set<Item> intersection = getTopKItems(parameters[2], k);
    	for(int i=3; i<parameters.length;i++)
    	{
    		intersection.retainAll(getTopKItems(parameters[i], k));
    	}
    	sortAndPrintQueryResult(intersection);
    }
    
    private static void sortAndPrintQueryResult(Set<Item> intersection)
    {
    	List<Item> sortedList = new ArrayList<Item>(intersection);
    	Collections.sort(sortedList, itemComparatorByScore);
    	for(int i=sortedList.size()-1;i>=0;i--)
    	{
    		System.out.print(sortedList.get(i).getId()+" ");
    	}
    	System.out.println();
    }
    
    private static Set<Item> getTopKItems(String queryString, int numOfResults)
    {
    	Set<Item> items = new HashSet<Item>();
    	if(queryString==null || queryString.isEmpty() || (numOfResults<1))
    		return items;
        items.addAll(trie.traverseTrie(queryString.trim().toLowerCase()));
        if(items.size()>numOfResults)
        {
            PriorityQueue<Item> minPriorityQueueByScore = new PriorityQueue<Item>(numOfResults, itemComparatorByScore);
            float minScore = 0;
            for(Item item : items)
            {
            	if(minPriorityQueueByScore.size()<numOfResults)
            	{
            		minPriorityQueueByScore.add(item);
            		minScore = minPriorityQueueByScore.peek().getScore();
            	}
            	else
            	{
            		if(item.getScore()>minScore)
            		{
            			minPriorityQueueByScore.poll();
            			minPriorityQueueByScore.add(item);
            			minScore = minPriorityQueueByScore.peek().getScore();
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
