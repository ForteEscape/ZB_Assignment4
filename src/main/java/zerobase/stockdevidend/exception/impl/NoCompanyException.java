package zerobase.stockdevidend.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdevidend.exception.AbstractException;

public class NoCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String tetMessage() {
        return "존재하지 않는 회사명 입니다.";
    }
}
