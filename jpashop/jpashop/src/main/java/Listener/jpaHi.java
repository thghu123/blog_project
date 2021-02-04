package Listener;

import jpabook.jpashop.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

public class jpaHi implements ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    public static MemberRepository memberRepository;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        domain.Member member = new domain.Member();
        member.setUsersname("BBB");

        //Long save =
        memberRepository.save(member);
        //domain.Member findMember = memberRepository.find(save);
        //System.out.println(findMember);


    }
}
