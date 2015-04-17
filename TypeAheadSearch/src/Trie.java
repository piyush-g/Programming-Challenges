import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Trie {
    TrieNode root;
    public Trie()
    {
        root = new TrieNode(' ',null);
    }
    public void addWord(String word, Item item)
    {
        TrieNode current = root;
        char[] char_arr = word.toCharArray();
        for(int i=0;i<word.length()-1;i++)
        {
            TrieNode child = addChildToTrieNode(current, char_arr[i],null);
            current = child;
        }
        addChildToTrieNode(current, char_arr[char_arr.length-1], item);
    }
    private TrieNode addChildToTrieNode(TrieNode node, char c, Item item)
    {
        TrieNode child = node.getChild(c);
        if(child==null){
            child = new TrieNode(c,item);
            node.addChild(child);
        }
        else
        {
            if(item!=null)
                child.addItem(item);
        }
        return child;
    }
    public void printTrie()
    {
        Set<Item> set = inorderTraverse(root);
        for(Item item: set)
        {
            System.out.println("name: "+item.getId()+" Type: "+item.getType());
        }
    }
    public Set<Item> traverseTrie(String prefix)
    {
        TrieNode node = traverseToNodeForPrefix(prefix);
        return inorderTraverse(node);
    }
    private TrieNode traverseToNodeForPrefix(String prefix)
    {
        if(prefix==null || prefix.isEmpty())
            return null;
        char[] char_arr = prefix.toCharArray();
        TrieNode current = root;
        for(int i=0; (current!=null && i<char_arr.length);i++)
        {
            current = current.getChild(char_arr[i]);
        }
        return current;
    }
    private Set<Item> inorderTraverse(TrieNode node)
    {
        if(node==null)
            return null;
        Set<Item> itemList = new HashSet<Item>(node.getItems());
        Map<Character, TrieNode> children = node.getChildren();
        for(char ch : children.keySet())
        {
            TrieNode child = children.get(ch);
            itemList.addAll(inorderTraverse(child));
        }
        return itemList;
    }
}
