
public class Item {
    public enum ItemType{
        USER, TOPIC, QUESTION, BOARD;
    }

    private static final String USER_VAL="user";
    private static final String TOPIC_VAL="topic";
    private static final String QUESTION_VAL="question";
    private static final String BOARD_VAL="board";

    private ItemType type;
    private String id;
    private int index;
    private String value;
    private float score;
    
    public Item(String itemType, int index, String id, float score, String val)
    {
        if(itemType==null || itemType.isEmpty() || id==null || id.isEmpty() || val==null || val.isEmpty())
            throw new IllegalArgumentException("Invalid item input: ");
        this.type = parseItemType(itemType);
        this.index = index;
        this.id = id;
        this.score = score;
        this.value = val;
    }
    
    public ItemType getType() {
        return type;
    }

    public int getIndex()
    {
    	return index;
    }
    public void setType(ItemType type) {
        this.type = type;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public float getScore() {
        return score;
    }


    public void setScore(float score) {
        this.score = score;
    }

    private ItemType parseItemType(String type)
    {
        if(type.equals(USER_VAL))
            return ItemType.USER;
        else if(type.equals(TOPIC_VAL))
            return ItemType.TOPIC;
        else if(type.equals(QUESTION_VAL))
            return ItemType.QUESTION;
        else if(type.equals(BOARD_VAL))
            return ItemType.BOARD;
        else 
        {
            throw new IllegalArgumentException("Invalid item type");
        }
    }
    public String getItemType(){
    	if(type.equals(ItemType.USER))
    		return USER_VAL;
    	else if(type.equals(ItemType.TOPIC))
    		return TOPIC_VAL;
    	else if(type.equals(ItemType.QUESTION))
    		return QUESTION_VAL;
    	else if(type.equals(ItemType.BOARD))
    		return BOARD_VAL;
    	return null;
    }
}