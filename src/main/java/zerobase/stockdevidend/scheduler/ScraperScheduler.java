package zerobase.stockdevidend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.stockdevidend.domain.CompanyEntity;
import zerobase.stockdevidend.domain.DividendEntity;
import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.ScrapResult;
import zerobase.stockdevidend.model.constans.CacheKey;
import zerobase.stockdevidend.repository.CompanyRepository;
import zerobase.stockdevidend.repository.DividendRepository;
import zerobase.stockdevidend.scraper.Scrapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scrapper yahooFinanceScraper;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scrapping started");

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity company: companies){
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapResult scrapResult = yahooFinanceScraper.scrap(
                    Company.builder()
                            .name(company.getName())
                            .ticker(company.getTicker())
                            .build()
            );

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장

            scrapResult.getDividendEntities().stream()
                    // mapping dividend entity from dividend model
                    .map(e -> new DividendEntity(company.getId(), e))
                    // insert each dividend entity to dividend repository
                    .forEach(e -> {
                        boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());

                        if (!exists){
                            dividendRepository.save(e);
                        }
                    });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
