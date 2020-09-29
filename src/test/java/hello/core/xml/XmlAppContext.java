package hello.core.xml;

import hello.core.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 설정 형식
 * 1. 자바 코드 -> AnnotationConfigApplicationContext
 * 2. xml 설정 -> GenericXmlApplictionContext 사용을 통해 xml 설정 파일을 넘김
 * - xml 기반의 appConfig.xml 스프링 설정 정보와 자바 코드로 된 AppConfig.java 설정 정보를 비교해보 면 거의 비슷함.
 * 3. 임의의 형식으로 만들어 사용
 */
class XmlAppContext {

    @Test
    void xmlAppContext() {
        ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
