package zerobase.stockdevidend.scraper;

import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.ScrapResult;

public interface Scrapper {
    Company scrapCompanyByTicker(String ticker);
    ScrapResult scrap(Company company);
}
