package zerobase.stockdevidend.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.Dividend;
import zerobase.stockdevidend.model.ScrapResult;
import zerobase.stockdevidend.model.constans.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class YahooFinanceScraper implements Scrapper{

    private static final String STATIC_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400;

    @Override
    public ScrapResult scrap(Company company){
        ScrapResult scrapResult = new ScrapResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;

            System.out.println(now);

            String urlResult = String.format(STATIC_URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(urlResult);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableElement = parsingDivs.get(0);

            Element tbody = tableElement.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String text = e.text();

                if (!text.endsWith("Dividend")){
                    continue;
                }

                StringTokenizer st = new StringTokenizer(text);

                String monthStr = st.nextToken();
                int month = Month.strToNumber(monthStr);
                int day = Integer.parseInt(st.nextToken().replace(",", ""));
                int year = Integer.parseInt(st.nextToken());
                String dividend = st.nextToken();

                if (month < 0){
                    throw new RuntimeException("Unexpected Month enum value -> " + monthStr);
                }

                dividends.add(
                        Dividend.builder()
                                .date(LocalDateTime.of(year, month, day, 0, 0))
                                .dividend(dividend)
                                .build()
                );
            }

            scrapResult.setDividendEntities(dividends);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL, ticker, ticker);
        try {
            Document document = Jsoup.connect(url).get();
            Element titleElement = document.getElementsByTag("h1").get(0);

            String title = titleElement.text().split(" - ")[1].trim();

            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
