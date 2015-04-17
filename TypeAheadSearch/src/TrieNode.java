import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrieNode {
    private char ch;
    private List<Item> itemList;
    private Map<Character, TrieNode> children;
    public TrieNode(char ch, Item item)
    {
        this.ch=ch;
        itemList = new ArrayList<Item>();
        children = new HashMap<Character, TrieNode>();
        if(item!=null)
            itemList.add(item);
    }
    public void addItem(Item item)
    {
        itemList.add(item);
    }
    public List<Item> getItems() {
        return itemList;
    }
    public char getValue()
    {
        return ch;
    }
    public void addChild(TrieNode node)
    {
        children.put(node.getValue(), node);
    }
    public TrieNode getChild(char ch)
    {
        if(children.containsKey(ch))
            return children.get(ch);
        return null;
    }
    public Map<Character, TrieNode> getChildren()
    {
        return children;
    }
}
