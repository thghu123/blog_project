package jpabook.jpashop;

import domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember(){ //원래 Test는 실행 후 롤백을 하는 데 false를 하면 데이터를 commit한다.
        //JPA 세팅 확인 완료.

        //given
        Member member = new Member();
        member.setUsersname("AAA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsersname()).isEqualTo(member.getUsersname());

        //트랜젝션 어노테이션이 없기에 패일
        Assertions.assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member: " +(findMember==member));
        //위의 멤버가 같은지를 확인한다. 같다 -> findMember 영속성 트랜젝션에서 id가 같으면 같은 걸로 인식한다.

    }


}