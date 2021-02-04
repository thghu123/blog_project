package jpabook.jpashop;

import domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository //기본 콤포넌트 스캔의 대상이 되는 어노테이션
public class MemberRepository {

    @PersistenceContext
    EntityManager em;//엔티티 메니져를 생성해서 주입해준다.

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
