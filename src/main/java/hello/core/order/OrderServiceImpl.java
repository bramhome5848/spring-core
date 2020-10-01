package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    /**
     * 할인 정책 변경 fix -> rate
     * 문제점
     * 추상(인터페이스) 뿐만 아니라 구체(구현) 클래스에도 의존 -> DIP 위반
     * FixDiscountPolicy 를 RateDiscountPolicy 로 변경하는 순간 OrderServiceImpl 의 소스 코드도 함께 변경 -> OCP 위반
     *
     * DIP 위반 -> 추상에만 의존하도록 변경(인터페이스에만 의존)
     * 구현체가 없는 코드 -> null pointer exception
     * 문제를 해결하려면 누군가가 클라이언트인 OrderServiceImpl 에 DiscountPolicy 의 구현 객체를 대신 생성하고 주입해어야 함
     *
     * 인터페이스 -> 배역, 구현체 -> 배우
     * 기존 방식 -> 특정 역할에 특정 배우를 직접 초빙하는 것과 같음, 배역이 공연도 해야하고 배우도 직접 초빙하는 다양한 책임을 가짐
     *
     * 관심사의 분리가 필요
     * 배우는 본인의 배역을 수행하는 것에 집중, 공연을 구성하고 배우를 섭외하고 역할에 맞는 배우를 지정하는 공연기획자 역할이 필요
     * 공연 기획자를 만들고, 배우와 공연 기획자의 책임을 분리
     * AppConfig -> 공연 기획자 역할
     */

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    //생성자를 통해서 주입
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
