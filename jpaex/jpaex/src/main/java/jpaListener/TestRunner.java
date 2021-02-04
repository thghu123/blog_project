package jpaListener;

import entity.SysStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import repository.SysStatRepository;

@Component
public class TestRunner implements ApplicationRunner {

    //@Autowired
    //SysStatRepository sysStatRepository;

    private final SysStatRepository sysstatRepository;

    @Autowired
    public TestRunner(SysStatRepository sysstatRepository) {
        this.sysstatRepository = sysstatRepository;
    }

    @Autowired
    public void run(ApplicationArguments args) throws Exception {
        SysStat sysstat1 = new SysStat("test1");
        SysStat sysstat2 = new SysStat("test2");

        //시간만 저장하는 방법
        sysstatRepository.save(sysstat1);
        sysstatRepository.save(sysstat2);


    }

}
