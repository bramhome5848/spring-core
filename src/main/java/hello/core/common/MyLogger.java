package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * 로그를 출력하기 위한 MyLogger 클래스
 * @Scope(value = "request") 를 사용해서 request 스코프로 지정
 * HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸
 * 스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성해서 주입이 가능하지만,
 * request 스코프 빈은 아직 생성되지 않음. 이 빈은 실제 고객의 요청이 와야 생성할 수 있음
 */

/**
 * proxyMode
 * 적용 대상이 인터페이스가 아닌 클래스면 TARGET_CLASS 를 선택
 * 적용 대상이 인터페이스면 INTERFACES 를 선택
 * MyLogger 가짜 프록시 클래스를 만들어두고 HTTP request 와 상관없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있음
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create : " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close : " + this);
    }

}
