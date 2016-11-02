
public class ZillowParser{
	public ZillowParser(){}
	public House[] getZillowSample(String address){
		//search the address/zip on Zillow and get result page
		//
		//
		String mapViewUrl = "http://www.zillow.com/homes/02067_rb/";
		int sampleNumber;
		String[] sampleUrls = mapViewParser(mapViewUrl);
		House[] hs = new House[sampleUrls.length];
		for(int i = 0; i < sampleUrls.length; i++){
			hs[i] = singleHouseParser(sampleUrls[i]);
		}
		return hs;
	}
	public House singleHouseParser(String url){
		//parse single house page and build a house object
		//house condiction
		int floorSize;
		int lot;
		int year;
		int bedNumber;
		int bathNumber;
		//address
		String zipcode;
		String state;
		String city;
		String address;
		//price
		int salePrice;

		//parse the page and get info

		House h = new House();
		return h;
	}
	public String[] mapViewParser(String url){
		//get single house url
		//http://www.zillow.com/homes/02067_rb/
		//http://www.zillow.com/homes/02067_rb/2_p/
		//...
		//lose data point when area is too big
		//how filter on the page working?
		String[] ss = new String[1];
		return ss;
	}
}
