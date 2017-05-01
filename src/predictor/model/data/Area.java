package predictor.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable{

	private static final long serialVersionUID = 1L;

	public List<House> HouseList;
	
	public Area(){
		HouseList = new ArrayList<House>();
	}
	
	public Area(List<House> newList){
		HouseList = new ArrayList<House>(newList);
	}
	
	public void add(House h){
		HouseList.add(h);
	}
	
	public void replaceList(ArrayList<House> newList){
		HouseList = new ArrayList<House>(newList);
	}
}
