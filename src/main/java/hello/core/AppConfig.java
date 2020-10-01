package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션의 전체 동작 방식을 구성(config)하기 위해, 구현 객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스
 * 중복을 제거 및 역할에 따른 구현이 보이도록 리팩터링
 * 스프링으로 변경
 */
@Configuration  // 설정 정보
public class AppConfig {

    //@Bean memberService -> new MemoryMemberRepository()
    //@Bean orderService -> new MemoryMemberRepository()
    //2번 호출 -> 싱글톤이 깨지는거 아닌가??

    //ConfigurationSingletonTest 의 configurationTest 실행시
    //예상했던 call message
    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.memberRepository
    //call AppConfig.orderService
    //call AppConfig.memberRepository

    //실제 결과 call message
    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.orderService

    /**
     * @Configuration 을 적용하지 않고, @Bean 만 적용하면 어떻게 될까
     * CGLIB 기술 없이 순수한 AppConfig로 스프링 빈에 등록
     * @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
     * 계속해서 new 키워드를 통해 instance 생성
     */

    @Bean   //스프링 컨테이너에 스프링 빈으로 등록 -> 기본 메서드 이름으로 등록
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();    //할인 정책 변경, 구성 영역의 코드만 수정하면 됨, 사용 영역의 코드는 수정할 필요 없음
    }
}
