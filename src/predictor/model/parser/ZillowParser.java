package predictor.model.parser;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import predictor.model.data.House;

public class ZillowParser {

	// Return an ArrayList of Houses in the input area
	// IOException, FailingHttpStatusCodeException, MalformedURLException
    public static ArrayList<House> parseArea(Double latitude, Double longitude, Double latitudeRange, Double longitudeRange) throws Exception {
        // http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/42.130232,-71.153255,42.10139,-71.193381_rect/14_zm/2_p/
//    	42.444456 - 42.402259 = 0.042197
//    	-71.154714 - -71.211706 = 0.056992
        ArrayList<House> houses = new ArrayList<House>();
        DecimalFormat formatter = new DecimalFormat("0.000000");
        
        // count coordinate into certain format String
        String up = formatter.format(latitude + latitudeRange / 2);
        String down = formatter.format(latitude - latitudeRange / 2);
        String right = formatter.format(longitude + longitudeRange / 2);
        String left = formatter.format(longitude - longitudeRange / 2);

        // make URL of target area
        String leftUrl = "http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/12m_days/globalrelevanceex_sort/";
        String middleUrl = up + "," + right + "," + down + "," + left;
        String rightUrl = "_rect/14_zm/";

        int parsePages = 1;

        for(int i = 1; i <= parsePages; i++){
            String url = leftUrl + middleUrl + rightUrl + i + "_p";
            System.err.println("get list of nearby houses from: "+ url);
            Document udoc;
            Elements urlElements;
            
            
        	WebClient wc = new WebClient();
            wc.getOptions().setJavaScriptEnabled(true);
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setThrowExceptionOnScriptError(false);
            wc.getOptions().setTimeout(10000);
            
            try{
	            HtmlPage page = wc.getPage(url);
	            int errorCount = 0;
	            while(true){
	                udoc = Jsoup.parse(page.asXml(), "http://www.zillow.com");
	                urlElements = udoc.select("a[class=\"zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link\"]");
	                if(urlElements.isEmpty() && errorCount++ < 200){
	                		Thread.sleep(200);
	                }
	                else{
	                    break;
	                }
	            }
            }
            finally{
            	wc.close();
            }

            // get page number
            if(i == 1){
                Elements pageSelectElements = udoc.select("ol[class=\"zsg-pagination\"]").select("li");
                if(!pageSelectElements.isEmpty() && pageSelectElements.size() > 1){
                    parsePages = Integer.parseInt(pageSelectElements.get(pageSelectElements.size() - 2).text());
                }
            }


            for(Element u : urlElements){
                String houseUrl = u.attr("abs:href");
                // System.err.println(houseUrl);
                House tempHouse;
                try{
                	tempHouse = parseHouseDetailPage(houseUrl);
                }
                catch(Exception e){
                	//error occur while parsing 
                	//abandon parsing
                	tempHouse = null;
                }
                if(tempHouse != null){
            		houses.add(tempHouse);
            	}
            }
        }
        return houses;
    }
	
	// Return an ArrayList of Houses in the input zip area
	// IOException, FailingHttpStatusCodeException, MalformedURLException
    public static ArrayList<House> parseZip(String zip) throws Exception {
        // https://www.zillow.com/homes/recently_sold/02125_rb/house,apartment_duplex,townhouse_type/
    	
        ArrayList<House> houses = new ArrayList<House>();

        // make URL of target area
        String leftUrl = "https://www.zillow.com/homes/recently_sold/";
        String middleUrl = zip;
        String rightUrl = "_rb/house,townhouse_type/24m_days/";

        int parsePages = 1;

        for(int i = 1; i <= parsePages; i++){
            String url = leftUrl + middleUrl + rightUrl + i + "_p";
            System.err.println("get list of nearby houses from: "+ url);
            Document udoc;
            Elements urlElements;
            Elements captchaElements;
            
            
        	WebClient wc = new WebClient();
            wc.getOptions().setJavaScriptEnabled(true);
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setThrowExceptionOnScriptError(false);
            wc.getOptions().setTimeout(10000);
            
            boolean parseResult = false;
            
            try{
	            HtmlPage page = wc.getPage(url);
	            int errorCount = 0;
	            while(true){
	                udoc = Jsoup.parse(page.asXml(), "http://www.zillow.com");
	                urlElements = udoc.select("a[class=\"zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link\"]");
	                captchaElements = udoc.select("div[class=\"error-text-content\"]");
	                if(captchaElements.isEmpty() == false){
	                	System.err.println(captchaElements.text());
	                }
	                if(urlElements.isEmpty() && errorCount++ < 200){
	                	System.err.println(errorCount);
	                	Thread.sleep(2000);
	                }
	                else{
	                	parseResult = true;
	                    break;
	                }
	            }
            }
            finally{
            	wc.close();
            }
            
            if(parseResult == false){
            	i--;
            	System.err.println("Fail");
            	Thread.sleep(1000000);
            	continue;
            }

            // get page number
            if(i == 1){
                Elements pageSelectElements = udoc.select("ol[class=\"zsg-pagination\"]").select("li");
                if(!pageSelectElements.isEmpty() && pageSelectElements.size() > 1){
                    parsePages = Integer.parseInt(pageSelectElements.get(pageSelectElements.size() - 2).text());
                }
            }


            for(Element u : urlElements){
                String houseUrl = u.attr("abs:href");
                System.err.println(houseUrl);
                House tempHouse;
                try{
                	Thread.sleep(5000);
                	tempHouse = parseHouseDetailPage(houseUrl);
                }
                catch(Exception e){
                	//error occur while parsing 
                	//abandon parsing
                	tempHouse = null;
                }
                if(tempHouse != null){
            		houses.add(tempHouse);
            	}

            }
        }
        return houses;
    }
    
    
	public static House parseHouseThrowAddress(String address, String state, String zip) throws Exception{
        // http://www.zillow.com/homes/18-Sandy-Ridge-Cir-Sharon-MA-02067_rb/
        String leftUrl = "http://www.zillow.com/homes/";
        String rightUrl = "_rb/";
        String middleUrl = address.replaceAll("[\\s]", "-") + "-" + state + "-" + zip;
        String url = leftUrl + middleUrl + rightUrl;
        return parseHouseDetailPage(url);
	}
	
	
	//throws exception when connection error
	//return null if missing important information on page
	//throws IOException
	public static House parseHouseDetailPage(String url) throws Exception{
		
        Document doc;
        House h = new House();

        //get house's page
        while(true){
        	doc = Jsoup.connect(url).get();
	        Elements captchaElements = doc.select("div[class=\"error-text-content\"]");
	        if(captchaElements.isEmpty() == false){
	        	System.err.println(captchaElements.text());
	        	Thread.sleep(1000000);
	        }
	        else{
	        	break;
	        }
        }

        
        Elements captchaElements = doc.select("div[class=\"error-text-content\"]");
        System.err.println(captchaElements.text());
        
        //////////////////
        // start Parsing//
        //////////////////
        
        // address, city, state, zip
        Elements addressElements = doc.select("header[class=\"zsg-content-header addr\"]").select("h1[class=\"notranslate\"]");
        System.err.println(addressElements.text());
        if(!addressElements.isEmpty() && addressElements.size() >= 1){
	        String fullAddress = addressElements.get(0).text();
	        if(!fullAddress.isEmpty() && fullAddress.split(",").length >= 3 && fullAddress.split(",")[2].split(" ").length >= 3){
		        h.address = fullAddress.split(",")[0].trim();
		        h.city = fullAddress.split(",")[1].trim();
		        h.state = fullAddress.split(",")[2].split(" ")[1].trim();
		        h.zip = fullAddress.split(",")[2].split(" ")[2].trim();
		        if(h.address.isEmpty() || h.city.isEmpty() || h.state.isEmpty() || h.zip.isEmpty()){
		        	return null;
		        }
		        System.out.println(h.address);
	        }
	        else{
	        	return null;
	        }
        }
        else{
        	return null;
        }
        
        //get coordinate
        Elements coordinatesElements;
        if(doc.select("div").is("div[class=\"zsgxw-subfooter\"]")){
            coordinatesElements = doc.select("div[class=\"zsgxw-subfooter\"]")
                            .select("div[class=\"zsg-subfooter-content\"]")
                            .select("div[class=\"zsg-layout-top\"]")
                            .select("span[itemprop=\"geo\"][itemtype=\"http://schema.org/GeoCoordinates\"]")
                            .select("meta");
        }
        else{
            coordinatesElements = doc.select("div[class=\"zsg-subfooter-content\"]")
                            .select("div[class=\"zsg-layout-top\"]")
                            .select("span[itemprop=\"geo\"][itemtype=\"http://schema.org/GeoCoordinates\"]")
                            .select("meta");
        }
        
        if(coordinatesElements.isEmpty() || coordinatesElements.size() != 2){
        	return null;
        }
        else{
        	try{
		        h.latitude = Double.parseDouble(coordinatesElements.get(0).attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
		        h.longitude = Double.parseDouble(coordinatesElements.get(1).attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
        	}
        	catch(NullPointerException | NumberFormatException e){
        		return null;
        	}
        }
        
        // get basic info: bed number, bath number, floor size
        Elements basicInfoElements = doc.select("header[class=\"zsg-content-header addr\"]")
                                        .select("h3")
                                        .select("span[class=\"addr_bbs\"]");
        
        if(basicInfoElements.isEmpty() || basicInfoElements.size() < 3){
        	return null;
        }
        else{
	        try{
		        h.bedroomNumber = Double.parseDouble(basicInfoElements.get(0).text().replaceAll("[^\\.^\\-^\\d]", ""));
		        h.bathroomNumber = Double.parseDouble(basicInfoElements.get(1).text().replaceAll("[^\\.^\\-^\\d]", ""));
		        h.floorSize = Double.parseDouble(basicInfoElements.get(2).text().replaceAll("[^\\.^\\-^\\d]", ""));
	        }
	        catch(NullPointerException | NumberFormatException e){
	        	return null;
	        }
        }
        
        //get sold price and date
        if(!doc.select("div[class=\"estimates\"]").select("div[class=\"main-row status-icon-row recently-sold-row home-summary-row\"]").isEmpty()){
        	Elements soldPriceElements = doc.select("div[class=\"estimates\"]")
                    .select("div[class=\"main-row status-icon-row recently-sold-row home-summary-row\"]")
                    .select("span[class=\"\"]");
        	if(!soldPriceElements.isEmpty()){
	            String lastSoldPrice = soldPriceElements.get(0).text().replaceAll("[^\\.^\\-^\\d]", "");
	            try{
	            	h.lastSoldPrice = Double.parseDouble(lastSoldPrice);
	            }
	            catch(NullPointerException | NumberFormatException e){
	            	// do nothing
	            	// some house does not have selling data
	            }
        	}
            
        	Elements soldDateElements = doc.select("div[class=\"estimates\"]")
                    .select("div[class=\" home-summary-row\"]")
                    .select("span[class=\"\"]");
        	
        	if(!soldDateElements.isEmpty()){
	            String lastSoldDateString = soldDateElements.get(0).text();
	            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
	            try{
	            	h.lastSoldDate = sdf.parse(lastSoldDateString);
	            }
	            catch(ParseException e){
	            	// do nothing
	            	// some house does not have selling data
	            }
        	}
        }
        
        //6 main features: type, year built, heating, cooling, parking, lot
        Elements mainFeatureElements = doc.select("div[class=\"hdp-facts-expandable-container clear\"]")
        							.select("div[class=\"zsg-g zsg-g_gutterless\"]")
        							.select("div[class=\"zsg-lg-1-3 zsg-md-1-2\"]");
        
        if(mainFeatureElements.isEmpty() || mainFeatureElements.size() < 6){
        	return null;
        }
        else{
        	//house type
        	h.houseType = mainFeatureElements.get(0).select("div[class=\"hdp-fact-ataglance-value\"]").text();
        	
        	//built year
        	
    		Elements builtYearElements = mainFeatureElements.get(1).select("div[class=\"hdp-fact-ataglance-value\"]");
    		if(!builtYearElements.isEmpty()){
    			try{
    				h.builtYear = Integer.parseInt(builtYearElements.text());
    			}
    			catch(NumberFormatException e){
            		//do nothing
            	}
    		}
        	
	        if(!mainFeatureElements.get(2).select("div[class=\"hdp-fact-ataglance-value\"]").isEmpty()){
	        	h.heating = mainFeatureElements.get(2).select("div[class=\"hdp-fact-ataglance-value\"]").text();
	        }
	        if(!mainFeatureElements.get(3).select("div[class=\"hdp-fact-ataglance-value\"]").isEmpty()){
	        	h.cooling = mainFeatureElements.get(3).select("div[class=\"hdp-fact-ataglance-value\"]").text();
	        }
	        if(!mainFeatureElements.get(4).select("div[class=\"hdp-fact-ataglance-value\"]").isEmpty()){
	        	h.parking = mainFeatureElements.get(4).select("div[class=\"hdp-fact-ataglance-value\"]").text();
	        }


	        String lotSizeText = mainFeatureElements.get(5).text();

	        try{
		        if(lotSizeText.matches("(.*)acres")){
		            //1acre = 43560sqft
		            h.lotSize = Double.parseDouble(lotSizeText.replaceAll("[^\\.^\\-^\\d]", "")) * 43560;
		        }
		        else if(lotSizeText.matches("(.*)sqft")){
		            h.lotSize = Double.parseDouble(lotSizeText.replaceAll("[^\\.^\\-^\\d]", ""));
		        }
		        else{
		        	h.lotSize = 0;
		        }
	        }
	        catch(NumberFormatException e){
	        	h.lotSize = 0;
	        }
	        
        }
        
        Elements allFeatureElements = doc.select("div[class=\"hdp-facts-expandable-container clear\"]")
				.select("div[class=\"z-moreless-content hdp-fact-moreless-content collapse\"]")
				.select("div[class=\"hdp-fact-container-columns\"]")
				.select("div[class=\"hdp-fact-container\"]")
				.select("ul[class=\"zsg-sm-1-1 hdp-fact-list\"]")
				.select("li[class=\"\"]");
		h.features = new String[allFeatureElements.size()]; 
		for(int i = 0; i < allFeatureElements.size(); i++){
			h.features[i] = allFeatureElements.get(i).text();
//			System.out.println(h.features[i]);
		}
		
        
		Elements allDiscription = doc.select("div[class=\"notranslate zsg-content-item\"]");
		Elements halfShowedDiscription = allDiscription.clone();
		halfShowedDiscription.select("span").remove();
		Elements halfHideDiscription = allDiscription.select("span[class=\"hide\"]");
//		System.out.println(halfShowedDiscription.text() + halfHideDiscription.text());
		h.discription = halfShowedDiscription.text() + halfHideDiscription.text();
		
		return h;
	}
	
}
