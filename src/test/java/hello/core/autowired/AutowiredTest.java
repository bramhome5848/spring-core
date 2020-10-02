package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * 옵션 처리
 * 주입할 스프링 빈이 없어도 동작해야 할 때가 있음
 * 그런데 @Autowired 만 사용하면 required 옵션의 기본값이 true 로 되어 있어서 자동 주입 대상이 없 으면 오류가 발생
 */
class AutowiredTest {

    @Test
    @Autowired
    public void setNoBean1() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {

        //호출 안됨 -> 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
        //Member -> 스프링 빈이 아님
        @Autowired(required = false)
        public void setNoBean1(Member member) {
            System.out.println("setNoBean1 = " + member);
        }

        //null 호출 -> 자동 주입할 대상이 없으면 null 이 입력됨
        @Autowired
        public void setNoBean2(@Nullable Member member) {
            System.out.println("setNoBean2 = " + member);
        }

        //Optional.empty 호출 -> 자동 주입할 대상이 없으면 Optional.empty 가 입력됨
        @Autowired(required = false)
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 = " + member);
        }

        /**
         *  @Nullable, Optional은 스프링 전반에 걸쳐서 지원
         *  예를 들어서 생성자 자동 주입에서 특정 필드에만 사용 가능
         */
    }
}
