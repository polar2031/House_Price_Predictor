import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.String;

import java.io.IOException;

public class HouseUrlParser{
    public static void main(String[] args) throws IOException {
        String leftUrl = "http://www.zillow.com/homes/recently_sold/";
        String middleUrl = "Town-of-Sharon-MA-02067";
        String rightUrl = "/house,condo,apartment_duplex,townhouse_type/11_zm/";
        for(int i = 1; i <= 20; i++){
            String url = leftUrl + middleUrl + rightUrl + i + "_p";
            Document doc = Jsoup.connect(url).get();
            Elements urlElements = doc.select("a[class=\"zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link\"]");
            for(Element u : urlElements){
                System.out.println(u.attr("abs:href"));
            }
        }
    }
}
