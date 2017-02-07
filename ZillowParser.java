import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.String;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.lang.Thread;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import java.util.logging.Logger;
import java.util.logging.Level;



public class ZillowParser{
    public ZillowParser(){
    }
    public boolean parseNearbyHouses(House h){
        // http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/42.130232,-71.153255,42.10139,-71.193381_rect/14_zm/2_p/
        // http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/42.112048,-71.145751,42.083248,-71.184751_rect/14_zm/1_p
        // http://www.zillow.com/homes/recently_sold/house,apartment_duplex,townhouse_type/globalrelevanceex_sort/42.112048,-71.145751,42.083248,-71.184751_rect/14_zm/1_p
        // 42.135515 - 42.106676 = 0.028839 -> +-0.0144
        // -71.154907 - -71.193874 = 0.038967 -> +-0.0195
        try{
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

            FileWriter urlData = new FileWriter("./houseUrl/" + h.zip + ".txt");
            int id = 0;
            int parsePages = 1;

            Logger logger = Logger.getLogger("com.gargoylesoftware");
            logger.setLevel(Level.OFF);

            for(int i = 1; i <= parsePages; i++){
                String url = leftUrl + middleUrl + rightUrl + i + "_p";
                System.err.println(url);

                WebClient wc = new WebClient();
                wc.getOptions().setJavaScriptEnabled(true);
                wc.getOptions().setCssEnabled(false);
                wc.getOptions().setThrowExceptionOnScriptError(false);
                wc.getOptions().setTimeout(10000);
                HtmlPage page = wc.getPage(url);
                JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();

                // while(manager.getJobCount() > 30){
                //     Thread.sleep(1000);
                // }
                // System.err.println(page.getUrl());

                Document udoc;
                Elements urlElements;
                while(true){
                    udoc = Jsoup.parse(page.asXml(), "http://www.zillow.com");
                    urlElements = udoc.select("a[class=\"zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link\"]");
                    if(urlElements.isEmpty()){
                        Thread.sleep(200);
                    }
                    else{
                        break;
                    }
                }
                wc.close();

                if(i == 1){
                    Elements pageSelectElements = udoc.select("ol[class=\"zsg-pagination\"]").select("li");
                    if(!pageSelectElements.isEmpty() && pageSelectElements.size() > 1){
                        // System.err.println(pageSelectElements.get(pageSelectElements.size() - 2).text());
                        parsePages = Integer.parseInt(pageSelectElements.get(pageSelectElements.size() - 2).text());
                    }
                }

                for(Element u : urlElements){
                    String houseUrl = u.attr("abs:href");
                    System.err.println(houseUrl);
                    House tempHouse = new House();
                    if(parseHouseDetailPage(tempHouse, houseUrl)){
                        id += 1;
                        FileWriter houseData = new FileWriter("./houseData/" + tempHouse.longtitute + "_" + tempHouse.longtitute + "_" + id + ".txt");
                        tempHouse.writeToFile(houseData);
                        houseData.close();
                        urlData.write(houseUrl + "\n");
                    }
                    else{
                        continue;
                    }
                }

            }
            urlData.close();
        }
        catch(IOException ioe){
            System.err.println("file_access_error");
            return false;
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean parseSameCityHouses(House h){
        try{
            String leftUrl = "http://www.zillow.com/homes/recently_sold/";
            String middleUrl = h.city + "-" + h.state + "-" + h.zip;
            String rightUrl = "/house,condo,apartment_duplex,townhouse_type/11_zm/";
            FileWriter urlData = new FileWriter("./houseUrl/" + h.zip + ".txt");
            int id = 0;
            int parsePages = 2;

            for(int i = 1; i <= parsePages; i++){
                String url = leftUrl + middleUrl + rightUrl + i + "_p";
                Document udoc = Jsoup.connect(url).get();
                Elements urlElements = udoc.select("a[class=\"zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link\"]");
                for(Element u : urlElements){
                    String houseUrl = u.attr("abs:href");
                    House tempHouse = new House();
                    if(parseHouseDetailPage(tempHouse, houseUrl)){
                        id += 1;
                        FileWriter houseData = new FileWriter("./houseData/" + tempHouse.longtitute + "_" + tempHouse.longtitute + "_" + id + ".txt");
                        tempHouse.writeToFile(houseData);
                        houseData.close();
                        urlData.write(houseUrl + "\n");
                    }
                    else{
                        continue;
                    }
                }
            }
            urlData.close();
        }
        catch(IOException ioe){
            System.err.println("file_access_error");
            return false;
        }
        return true;
    }

    public boolean parseHouse(House h){
        // http://www.zillow.com/homes/18-Sandy-Ridge-Cir-Sharon-MA-02067_rb/
        String leftUrl = "http://www.zillow.com/homes/";
        String rightUrl = "_rb/";
        String middleUrl = h.address.replaceAll("[\\s]", "-") + "-" + h.state + "-" + h.zip;
        String url = leftUrl + middleUrl + rightUrl;
        return parseHouseDetailPage(h, url);
    }

    private boolean parseHouseDetailPage(House h, String url){
        try{
            Document doc = Jsoup.connect(url).get();

            // get full address
            Elements addressElements = doc.select("div[class=\"zsg-layout-top\"]")
                                        .select("span[itemprop=\"address\"]")
                                        .select("a")
                                        .select("span");
            h.address = addressElements.get(0).text();
            h.city = addressElements.get(1).text();
            h.state = addressElements.get(2).text();
            h.zip = addressElements.get(3).text();

            if(h.address == null || h.city == null || h.state == null || h.zip == null){
                return false;
            }


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

            //get sold price and date
            h.lastSoldPrice = Double.parseDouble(
                                doc.select("div[class=\"estimates\"]")
                                .select("div[class=\"main-row status-icon-row recently-sold-row home-summary-row\"]")
                                .select("span[class=\"\"]")
                                .first()
                                .text()
                                .replaceAll("[^\\.^\\-^\\d]", ""));
            h.lastSoldDate = doc.select("div[class=\"estimates\"]")
                                .select("div[class=\" home-summary-row\"]")
                                .select("span[class=\"\"]")
                                .first()
                                .text();
            //get coordinate
            Elements coordinates = doc.select("div[class=\"zsg-layout-top\"]")
                                    .select("span[itemprop=\"geo\"][itemtype=\"http://schema.org/GeoCoordinates\"]")
                                    .select("meta");
            h.latitute = Double.parseDouble(coordinates.first().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
            h.longtitute = Double.parseDouble(coordinates.last().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));

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
        //Abandon when error occur (e.g. house with no useful info)
        catch(IndexOutOfBoundsException ioobe){
            return false;
        }
        catch(NullPointerException npe){
            return false;
        }
        catch(NumberFormatException nfe){
            return false;
        }
        catch(IOException ioe){
            return false;
        }
        return true;
    }
}
