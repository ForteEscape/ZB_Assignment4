package zerobase.stockdevidend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.ScrapResult;
import zerobase.stockdevidend.scraper.Scrapper;
import zerobase.stockdevidend.scraper.YahooFinanceScraper;

import java.io.IOException;
import java.util.StringTokenizer;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class StockdevidendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockdevidendApplication.class, args);
	}

}
