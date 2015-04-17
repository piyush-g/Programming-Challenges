import java.util.ArrayList;
import java.util.Comparator;
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
        if(command==null || command.isEmpty())
            return;
        if(command.startsWith(ADD_COMMAND))
        {
            addItem(command);
        }
    }
    
    /*private static void query(String queryString, int numOfResults)
    {
        Item[] items = (Item[]) trie.traverseTrie(queryString.toLowerCase()).toArray();
        if(set.size()>numOfResults)
        {
            PriorityQueue<Item> minPriorityQueueByScore = new PriorityQueue<Item>(numOfResults, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    float score1 = o1.getScore();
                    float score2 = o2.getScore();
                    if(score1==score2)
                        return 0;
                    if((score1-score2)>0)
                        return 1;
                    else
                        return -1;
                }
            });
            for(int i=0;i)
        }
        for(Item item : set)
        {
            
        }
    }*/
    private static void addItem(String itemString)
    {
        Item item = parseAddCommand(itemString);
        System.out.println("Adding item: "+item.getId());
        itemList.add(item);
        trie.printTrie();
        System.out.println("---------------------------------");
    }
    private static Item parseAddCommand(String itemString)
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
        Item item = new Item(parameters[1],parameters[2], Float.parseFloat(parameters[3]), valueBuffer.toString());
        for(int i=4;i<parameters.length-1;i++)
        {
            trie.addWord(parameters[i].trim().toLowerCase(), item);
        }
        String lastWord = parameters[parameters.length-1];
        if(item.getType().equals(Item.ItemType.QUESTION))
        {
            lastWord = lastWord.trim().substring(0, lastWord.length()-1);
            System.out.println(lastWord);
        }
        trie.addWord(lastWord.trim().toLowerCase(), item);
        return item;
    }
}
