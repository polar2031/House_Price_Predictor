package predictor.model;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;

public class textAnalyzer {
	public List<Word> wordList;
	
	public textAnalyzer(){
		wordList = new ArrayList<Word>();
	}
	
	public void analysis(List<House> trainingHouseList){
		for(House h : trainingHouseList){
			String discription = h.discription;
			String[] discriptionStringArray = discription.split("\\W+");
			for(String s : discriptionStringArray){
				int index = search(s);
				if(index != -1){
					Word w = wordList.get(index);
					w.score = (w.score * w.count + (h.lastSoldPrice / h.floorSize)) / (w.count + 1);
					w.count++;
					System.out.println(s + " : " + w.count);
				}
				else{
					Word w = new Word(s);
					wordList.add(w);
					System.out.println(s + " added");
				}
			}
//			String[] featureArray = discription.split(",");
//			for(String fs : featureArray){
//				int index = search();
//			}
		}
	}
	
	private int search(String s){
		if(wordList.size() == 0){
			return -1;
		}
		else{
			for(int i = 0; i < wordList.size(); i++){
				if(wordList.get(i).wordString.compareTo(s) == 0){
					return i;
				}
			}
		}
		return -1;
	}
	
	
}
class Word {
	public String wordString;
	public double score;
	public int count;
	public Word(String s){
		wordString = s;
		score = 0;
		count = 0;
	}
}