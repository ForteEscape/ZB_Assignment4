package zerobase.stockdevidend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.StringTokenizer;

//@SpringBootApplication
public class StockdevidendApplication {

	public static void main(String[] args) {
		//SpringApplication.run(StockdevidendApplication.class, args);

		Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1685232000&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
		try {
			Document document = connection.get();

			Elements elements = document.getElementsByAttributeValue("data-test", "historical-prices");
			Element element = elements.get(0);

			Element tbody = element.children().get(1);
			for (Element e : tbody.children()) {
				String text = e.text();

				if (!text.endsWith("Dividend")){
					continue;
				}

				StringTokenizer st = new StringTokenizer(text);
				String month = "";
				int day = 0;
				int year = 0;
				String dividend = "";

				month = st.nextToken();
				day = Integer.parseInt(st.nextToken().replace(",", ""));
				year = Integer.parseInt(st.nextToken());
				dividend = st.nextToken();

				System.out.println(year + "/" + day + "/ " + month + " " + dividend);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


	}

}
