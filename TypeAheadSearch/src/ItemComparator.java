import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ItemComparator implements Comparator<Item>{
	Map<String, Float> boosterMap;
	ItemComparator(int n)
	{
		boosterMap = new HashMap<String, Float>(n);
	}
	@Override
	public int compare(Item item1, Item item2) {
		float score1 = computeScore(item1);
		float score2 = computeScore(item2);
		if(score1==score2)
			return item1.getIndex()-item2.getIndex();
		else if(score1>score2)
			return 1;
		else 
			return -1;
	}
	public void addBooster(String name, float value)
	{
		boosterMap.put(name.toLowerCase(), value);
	}
	private float computeScore(Item item)
	{
		float score = item.getScore();
		String id = item.getId().toLowerCase();
		String itemType = item.getItemType().toLowerCase();
		if(boosterMap.containsKey(id))
			score *= boosterMap.get(id);
		if(boosterMap.containsKey(itemType))
			score *= boosterMap.get(itemType);
		return score;
	}
}
