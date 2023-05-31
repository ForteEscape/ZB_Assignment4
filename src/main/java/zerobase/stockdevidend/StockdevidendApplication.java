package zerobase.stockdevidend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.ScrapResult;
import zerobase.stockdevidend.scraper.Scrapper;
import zerobase.stockdevidend.scraper.YahooFinanceScraper;

import java.io.IOException;
import java.util.StringTokenizer;

//@SpringBootApplication
public class StockdevidendApplication {

	public static void main(String[] args) {
		//SpringApplication.run(StockdevidendApplication.class, args);

		Scrapper scraper = new YahooFinanceScraper();
		Company mmm = scraper.scrapCompanyByTicker("MMM");
		System.out.println(mmm);
	}

}
