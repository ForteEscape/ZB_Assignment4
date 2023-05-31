package zerobase.stockdevidend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.stockdevidend.domain.CompanyEntity;
import zerobase.stockdevidend.domain.DividendEntity;
import zerobase.stockdevidend.model.Company;
import zerobase.stockdevidend.model.Dividend;
import zerobase.stockdevidend.model.ScrapResult;
import zerobase.stockdevidend.repository.CompanyRepository;
import zerobase.stockdevidend.repository.DividendRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapResult getDividendByCompanyName(String companyName){
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

        // 2. 조회된 회사 ID로 배당금 조회
        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

        // 3. 회명과 배당금 정보를 조합하여 ScrapResult 로 반환
        List<Dividend> dividends = new ArrayList<>();

        for (DividendEntity entity : dividendEntities){
            dividends.add(Dividend.builder()
                            .date(entity.getDate())
                            .dividend(entity.getDividend())
                            .build());
        }

        return new ScrapResult(Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(), dividends);
    }
}
