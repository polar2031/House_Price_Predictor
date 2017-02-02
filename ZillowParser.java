import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.String;
import java.io.FileWriter;
import java.io.IOException;

public class ZillowParser{
    public ZillowParser(){
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
