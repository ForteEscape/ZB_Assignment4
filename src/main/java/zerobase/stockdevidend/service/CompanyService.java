package zerobase.stockdevidend.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.stockdevidend.domain.CompanyEntity;
import zerobase.stockdevidend.domain.DividendEntity;
import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.ScrapResult;
import zerobase.stockdevidend.repository.CompanyRepository;
import zerobase.stockdevidend.repository.DividendRepository;
import zerobase.stockdevidend.scraper.Scrapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final Trie trie;
    private final Scrapper yahooFinanceScrapper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker){
        if (companyRepository.existsByTicker(ticker)){
            throw new RuntimeException("Already exists ticker in db -> " + ticker);
        }

        return storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker){
        // ticker를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScrapper.scrapCompanyByTicker(ticker);

        if (ObjectUtils.isEmpty(company)){
            throw new RuntimeException("failed to scrap ticker -> ticker name : " + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapResult scrapResult = yahooFinanceScrapper.scrap(company);

        // 스크래핑 결과
        CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntities);

        return company;
    }

    public Page<CompanyEntity> getAllCompany(final Pageable pageable){
        return companyRepository.findAll(pageable);
    }

    public void addAutoCompleteKeyword(String keyword){
        trie.put(keyword, null);
    }

    public List<String> autoComplete(String prefix){
        return (List<String>) trie.prefixMap(prefix).keySet().stream()
                .collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword){
        trie.remove(keyword);
    }

    public List<String> getCompanyNameByKeyword(String keyword){
        Pageable limit = PageRequest.of(0, 10);

        Page<CompanyEntity> result = companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);

        return result.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }
}
