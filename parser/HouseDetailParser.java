import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.String;

import java.io.IOException;

public class HouseDetailParser{
    public static void main(String[] args) throws IOException {
        String url = "http://www.zillow.com/homedetails/18-Sandy-Ridge-Cir-Sharon-MA-02067/57505799_zpid/";
        Document doc = Jsoup.connect(url).get();

        //get address
        Elements addressElements = doc.select("ol[id=\"gbc\"][class=\"zsg-breadcrumbs\"]")
                                    .select("a, span");
        String state = addressElements.get(0).text();
        String city = addressElements.get(1).text();
        String zip = addressElements.get(2).text();
        String street = addressElements.get(3).text();

        // get basic info
        Elements basicInfoElements = doc.select("header[class=\"zsg-content-header addr\"]")
                                        .select("h3")
                                        .select("span[class=\"addr_bbs\"]");
        Double bedNumber = Double.parseDouble(basicInfoElements.get(0).text().replaceAll("[^\\.^\\-^\\d]", ""));
        Double bathNumber = Double.parseDouble(basicInfoElements.get(1).text().replaceAll("[^\\.^\\-^\\d]", ""));
        Double floorSize = Double.parseDouble(basicInfoElements.get(2).text().replaceAll("[^\\.^\\-^\\d]", ""));

        //get sold price and date
        Double soldPrice = Double.parseDouble(
                            doc.select("div[class=\"estimates\"]")
                            .select("div[class=\"main-row status-icon-row recently-sold-row home-summary-row\"]")
                            .select("span[class=\"\"]")
                            .first()
                            .text()
                            .replaceAll("[^\\.^\\-^\\d]", ""));
        String soldDate = doc.select("div[class=\"estimates\"]")
                            .select("div[class=\" home-summary-row\"]")
                            .select("span[class=\"\"]")
                            .first()
                            .text();

        //get facts and features
        Elements factElements = doc.select("ul[class=\"zsg-list_square zsg-lg-1-3 zsg-md-1-2 zsg-sm-1-1\"]")
                                    .select("li[class=\"\"]");
        String facts[] = new String[factElements.size()];
        for(int i = 0; i < factElements.size(); i++){
            facts[i] = factElements.get(i).text();
        }

        //get coordinate
        Elements coordinates = doc.select("div[class=\"zsg-layout-top\"]")
                                .select("span[itemprop=\"geo\"][itemtype=\"http://schema.org/GeoCoordinates\"]")
                                .select("meta");
        Double latitute = Double.parseDouble(coordinates.first().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));
        Double longtitute = Double.parseDouble(coordinates.last().attr("content").replaceAll("[^\\.^\\-^\\d]", ""));

    }
}
