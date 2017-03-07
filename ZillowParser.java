import java.io.IOException;
import java.text.DecimalFormat;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.String;
import java.lang.Thread;
import java.util.*;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

public class ZillowParser{
    public ZillowParser(){
    }

    public ArrayList<House> parseNearbyHouses(House h){
        // http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/42.130232,-71.153255,42.10139,-71.193381_rect/14_zm/2_p/
        // 42.135515 - 42.106676 = 0.028839 -> +-0.0144
        // -71.154907 - -71.193874 = 0.038967 -> +-0.0195
        ArrayList<House> samples = new ArrayList<House>();

        // latitute and longtitute range
        Double latituteRange = 0.0144;
        Double longtituteRange = 0.0195;
        DecimalFormat formatter = new DecimalFormat("#.######");
        // formatter.applyPattern("0.000000");

        String up = formatter.format(h.latitute + latituteRange);
        String down = formatter.format(h.latitute - latituteRange);
        String left = formatter.format(h.longtitute + longtituteRange);
        String right = formatter.format(h.longtitute - longtituteRange);

        String leftUrl = "http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/";
        String middleUrl = up + "," + left + "," + down + "," + right;
        String rightUrl = "_rect/14_zm/";

        int parsePages = 1;

        try{
            for(int i = 1; i <= parsePages; i++){
                String url = leftUrl + middleUrl + rightUrl + i + "_p";
                System.out.println("get list of nearby houses from: "+ url);

                // stop all log from htmlunit
                Logger htmlUnitLogger = Logger.getLogger("com.gargoylesoftware");
                htmlUnitLogger.setLevel(Level.OFF);

                WebClient wc = new WebClient();
                wc.getOptions().setJavaScriptEnabled(true);
                wc.getOptions().setCssEnabled(false);
                wc.getOptions().setThrowExceptionOnScriptError(false);
                wc.getOptions().setTimeout(10000);
                HtmlPage page = wc.getPage(url);
                JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();

                Document udoc;
                Elements urlElements;
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

                // get page info
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
                    if(parseHouseDetailPage(tempHouse, houseUrl)){
                        samples.add(tempHouse);
                    }
                    else{
                        continue;
                    }
                }

            }
        }
        catch(Exception e){}
        return samples;
    }

    public boolean parseHouse(House h){
        // http://www.zillow.com/homes/18-Sandy-Ridge-Cir-Sharon-MA-02067_rb/
        String leftUrl = "http://www.zillow.com/homes/";
        String rightUrl = "_rb/";
        String middleUrl = h.address.replaceAll("[\\s]", "-") + "-" + h.state + "-" + h.zip;
        String url = leftUrl + middleUrl + rightUrl;
        // System.err.println(url);
        return parseHouseDetailPage(h, url);
    }

    public boolean parseHouseDetailPage(House h, String url){
        System.out.println("Get house detail from: " + url);
        Document doc;
        Elements addressElements;
        try{
            doc = Jsoup.connect(url).get();

            // get full address
            addressElements = doc.select("header[class=\"zsg-content-header addr\"]");
            String fullAddress = addressElements.select("h1").get(0).text();
            h.address = fullAddress.split(",")[0].trim();;
            h.city = fullAddress.split(",")[1].trim();
            h.state = fullAddress.split(",")[2].split(" ")[1].trim();
            h.zip = fullAddress.split(",")[2].split(" ")[2].trim();

            // System.out.println(h.address);
            // System.out.println(h.city);
            // System.out.println(h.state);
            // System.out.println(h.zip);

            if(h.address == null || h.city == null || h.state == null || h.zip == null){
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
        try{
            // get basic info
            Elements basicInfoElements = doc.select("header[class=\"zsg-content-header addr\"]")
                                            .select("h3")
                                            .select("span[class=\"addr_bbs\"]");
            h.bedroomNumber = Double.parseDouble(basicInfoElements.get(0).text().replaceAll("[^\\.^\\-^\\d]", ""));
            h.bathroomNumber = Double.parseDouble(basicInfoElements.get(1).text().replaceAll("[^\\.^\\-^\\d]", ""));
            h.floorSize = Double.parseDouble(basicInfoElements.get(2).text().replaceAll("[^\\.^\\-^\\d]", ""));

            if(basicInfoElements.get(0).text() == null || basicInfoElements.get(1).text() == null || basicInfoElements.get(2).text() == null){
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
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
            // System.out.println(h.lastSoldPrice);
            // System.out.println(lastSoldDateString);
        }
        catch(Exception e){}
        try{
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
            h.latitute = Double.parseDouble(coordinates.first().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
            h.longtitute = Double.parseDouble(coordinates.last().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
            // System.out.println(h.latitute);
            // System.out.println(h.longtitute);

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
        //Abandon when error occur
        catch(Exception e){}
        return true;
    }
}
