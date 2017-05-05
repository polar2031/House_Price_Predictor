package predictor.model.parser;

import java.io.IOException;
import java.text.DecimalFormat;
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
	
//    public static ArrayList<House> parseNearbyHouses(House h) throws Exception{
//        // 42.135515 - 42.106676 = 0.028839 -> +-0.0144
//        // -71.154907 - -71.193874 = 0.038967 -> +-0.0195
//        return parseArea(h.latitude, h.longitude, 0.015, 0.02);
//        // return this.parseNearbyHouses(h, 0.007, 0.01);
//    }
	
    public static ArrayList<House> parseArea(Double latitude, Double longitude, Double latitudeRange, Double longitudeRange) {
        // http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/42.130232,-71.153255,42.10139,-71.193381_rect/14_zm/2_p/

        ArrayList<House> houses = new ArrayList<House>();
        DecimalFormat formatter = new DecimalFormat("0.000000");

        String up = formatter.format(latitude + latitudeRange);
        String down = formatter.format(latitude);
        String right = formatter.format(longitude + longitudeRange);
        String left = formatter.format(longitude - longitudeRange);

        String leftUrl = "http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/";
        String middleUrl = up + "," + right + "," + down + "," + left;
        String rightUrl = "_rect/15_zm/";

        int parsePages = 1;

        for(int i = 1; i <= parsePages; i++){
            String url = leftUrl + middleUrl + rightUrl + i + "_p";
            System.err.println("get list of nearby houses from: "+ url);
            Document udoc;
            Elements urlElements;
            
            try{
            	WebClient wc = new WebClient();
                wc.getOptions().setJavaScriptEnabled(true);
                wc.getOptions().setCssEnabled(false);
                wc.getOptions().setThrowExceptionOnScriptError(false);
                wc.getOptions().setTimeout(10000);
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
                wc.close();
            }
            catch(IOException e){
            	e.printStackTrace();
            	return null;
            } catch (InterruptedException e) {
				e.printStackTrace();
				return null;
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
                House tempHouse = new House();
                try{
                	tempHouse = parseHouseDetailPage(houseUrl);
                	houses.add(tempHouse);
                }
                catch(Exception e){}
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
	
	public static House parseHouseDetailPage(String url) throws Exception{
//		System.err.println("Get house detail from: " + url);
		
		House h = new House();
        Document doc;
        Elements addressElements;

        doc = Jsoup.connect(url).get();

        // get full address
        addressElements = doc.select("header[class=\"zsg-content-header addr\"]");
        String fullAddress = addressElements.select("h1").get(0).text();
        h.address = fullAddress.split(",")[0].trim();;
        h.city = fullAddress.split(",")[1].trim();
        h.state = fullAddress.split(",")[2].split(" ")[1].trim();
        h.zip = fullAddress.split(",")[2].split(" ")[2].trim();
        
        if(h.address == null || h.city == null || h.state == null || h.zip == null){
        	throw new Exception("insufficient_data");
        }
        
        // get basic info
        Elements basicInfoElements = doc.select("header[class=\"zsg-content-header addr\"]")
                                        .select("h3")
                                        .select("span[class=\"addr_bbs\"]");
        
        if(basicInfoElements.get(2).text() == null || basicInfoElements.get(1).text() == null || basicInfoElements.get(0).text() == null){
        	throw new Exception("insufficient_data");
        }
        
        h.bedroomNumber = Double.parseDouble(basicInfoElements.get(0).text().replaceAll("[^\\.^\\-^\\d]", ""));
        h.bathroomNumber = Double.parseDouble(basicInfoElements.get(1).text().replaceAll("[^\\.^\\-^\\d]", ""));
        h.floorSize = Double.parseDouble(basicInfoElements.get(2).text().replaceAll("[^\\.^\\-^\\d]", ""));


        //get coordinate
        Elements coordinates;
        if(doc.select("div").is("div[class=\"zsgxw-subfooter\"]")){
            coordinates = doc.select("div[class=\"zsgxw-subfooter\"]")
                            .select("div[class=\"zsg-subfooter-content\"]")
                            .select("div[class=\"zsg-layout-top\"]")
                            .select("span[itemprop=\"geo\"][itemtype=\"http://schema.org/GeoCoordinates\"]")
                            .select("meta");
        }
        else{
            coordinates = doc.select("div[class=\"zsg-subfooter-content\"]")
                            .select("div[class=\"zsg-layout-top\"]")
                            .select("span[itemprop=\"geo\"][itemtype=\"http://schema.org/GeoCoordinates\"]")
                            .select("meta");
        }
        
        if(coordinates.first() == null || coordinates.last() == null){
        	throw new Exception("insufficient_data");
        }
        h.latitude = Double.parseDouble(coordinates.first().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
        h.longitude = Double.parseDouble(coordinates.last().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
        
        try{
            //get sold price and date
            h.lastSoldPrice = Double.parseDouble(
                                doc.select("div[class=\"estimates\"]")
                                .select("div[class=\"main-row status-icon-row recently-sold-row home-summary-row\"]")
                                .select("span[class=\"\"]")
                                .first()
                                .text()
                                .replaceAll("[^\\.^\\-^\\d]", ""));
            String lastSoldDateString = doc.select("div[class=\"estimates\"]")
                                .select("div[class=\" home-summary-row\"]")
                                .select("span[class=\"\"]")
                                .first()
                                .text();

            SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy");
            h.lastSoldDate = sdf.parse(lastSoldDateString);
        }
        catch(Exception e){
        	//do nothing
        }
        
        Elements featureElements = doc.select("div[class=\"hdp-facts-expandable-container clear\"]")
        							.select("div[class=\"zsg-g zsg-g_gutterless\"]")
        							.select("div[class=\"zsg-lg-1-3 zsg-md-1-2\"]");
        
        h.builtYear = Integer.parseInt(featureElements.get(1).select("div[class=\"hdp-fact-ataglance-value\"]").text());
        
        
        							
        /*
        try{
            //get features and features
            Elements featureElements = doc.select("ul[class=\"zsg-list_square zsg-lg-1-3 zsg-md-1-2 zsg-sm-1-1\"]")
                                        .select("li[class=\"\"]");
            h.features = new String[featureElements.size()];
            for(int j = 0; j < featureElements.size(); j++){
                h.features[j] = featureElements.get(j).text();
            }
            //get lot size from feature
            h.lotSize = 0;
            for(String feature : h.features){
            	System.err.println(feature);
                if(feature.matches("Lot:(.*)acres")){
                    //1acre = 43560sqft
                    h.lotSize = Double.parseDouble(feature.replaceAll("[^\\.^\\-^\\d]", "")) * 43560;
                }
                else if(feature.matches("Lot:(.*)sqft")){
                    h.lotSize = Double.parseDouble(feature.replaceAll("[^\\.^\\-^\\d]", ""));
                }
                else if (feature.matches("Built in (.*)")){
                	
                    h.builtYear = Integer.parseInt(feature.replaceAll("[^\\.^\\-^\\d]", ""));
                }
            }
        }
        catch(Exception e){
        	//do nothing
        }
        */
		return h;
	}
}
