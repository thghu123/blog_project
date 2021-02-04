package entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
public class SysStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //if를 자동으로 만들어준다는 의미이다.
    private Long id; //pk
    @Column(name="name")
    private String name;

    public SysStat(String name){
        this.name = name;
    }

}
