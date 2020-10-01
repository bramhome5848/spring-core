package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 컴포넌트 스캔을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록되기 때문에,
 * AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고 실행되어 버림
 * 그래서 excludeFilters 를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외
 * 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택
 *
 * 컴포넌트 스캔은 이름 그대로 @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록
 * > 참고: @Configuration 이 컴포넌트 스캔의 대상이 된 이유도 @Configuration 소스코드를 열어보면 @Component 애노테이션이 붙어있기 때문
 *
 * 컴포넌트 스캔의 기본 대상 & 부가 기능을 수행
 * @Component : 컴포넌트 스캔에서 사용
 * @Controlller : 스프링 MVC 컨트롤러에서 사용, 스프링 MVC 컨트롤러로 인식
 * @Service : 스프링 비즈니스 로직에서 사용, 특별한 처리를 하지 않고 대신 개발자들이 핵심 비즈니스 로직의 위치를 인식하는데 도움
 * @Repository : 스프링 데이터 접근 계층에서 사용, 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환
 * @Configuration : 스프링 설정 정보에서 사용, 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리
 */
@Configuration
@ComponentScan(
        //탐색할 패키지의 시작 위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색
        //basePackages = "hello.core.member",
        //지정한 클래스의 패키지를 탐색 시작 위로 지정, 만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치
        //basePackageClasses = AutoAppConfig.class,
        //권장방법 -> 패키지를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것
        //@ComponentScan 이 붙은 설정 정보 클래스를 최상단에 두고 해당 클래스의 패키지가 시작 위치가 되도록 하는 방법
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
public class AutoAppConfig {

    /**
     * 수동 빈 등록 vs 자동 빈 등록
     * 이 경우 수동 빈 등록이 우선권을 가짐 -> 수동 빈이 자동 빈을 오버라이딩 해버림
     * 개발자가 의도적으로 설정해서 이런 결과가 만들어지기 보다는 여러 설정들이 꼬여서 이런 결과가 만들어지는 경우가 대부분
     * 정말 잡기 어려운 버그!! 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 설정되어 있음
     */
    @Bean(name = "memoryMemberRepository")
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
