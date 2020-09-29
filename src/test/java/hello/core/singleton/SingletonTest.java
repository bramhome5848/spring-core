package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성
 * 고객 트래픽이 초당 100이 나오면 초당 100개 객체가 생성되고 소멸 -> 메모리 낭비
 * 객체가 딱 1개만 생성되고, 공유하도록 설계 -> 싱글톤
 */

/**
 * 싱글톤 패턴 문제점
 * 싱글톤 패턴을 구현하는 코드 자체가 많이 들어감 (싱글톤을 위한 추가적인 코드가 필요)
 * 의존관계상 클라이언트가 구체 클래스에 의존 -> 구체클래스.getInstance -> DIP를 위반
 * 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높음.
 * 테스트하기 어려움
 * 내부 속성을 변경하거나 초기화 하기 어려움
 * private 생성자로 자식 클래스를 만들기 어려움
 * 결론적으로 유연성이 떨어짐, 안티패턴으로 불리기도 함
 *
 * 싱글톤 컨테이너 -> 싱글톤 패턴의 문제를 해결하면서, 객체를 싱글톤으로 관리함
 */
class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();

        //1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        //2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    public void singletonServiceTest() {
        //private으로 생성자를 막음 -> 컴파일 오류
        //new SingletonService();

        //1. 조회: 호출할 때 마다 같은 객체를 반환
        SingletonService singletonService1 = SingletonService.getInstance();

        //2. 조회: 호출할 때 마다 같은 객체를 반환
        SingletonService singletonService2 = SingletonService.getInstance();

        //참조값이 같은 것을 확인
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        // singletonService1 == singletonService2
        assertThat(singletonService1).isSameAs(singletonService2);
        singletonService1.logic();
    }

    /**
     * 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤(1개만 생성)으로 관리
     * 스프링 빈이 바로 싱글톤으로 관리되는 빈이다.
     *
     * 싱글톤 컨테이너
     * 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리
     * 스프링 컨테이너는 싱글톤 컨테이너 역할을 하고 이렇게 싱글톤 객체를 생성하고 관리하는 기능 -> 싱글톤 레지스트
     * 스프링 컨테이너의 이런 기능 덕분에 싱글턴 패턴의 모든 단점을 해결하고 객체를 싱글톤으로 유지 가능
     * 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 됨
     * DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용할 수 있음
     *
     * 스프링의 기본 빈 등록 방식은 싱글톤이지만, 이외에 요청할 때 마다 객체를 생성해서 반환하는 기능도 제공
     */
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1. 조회: 호출할 때 마다 같은 객체를 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        //2. 조회: 호출할 때 마다 같은 객체를 반환
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService1 == memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }
}
