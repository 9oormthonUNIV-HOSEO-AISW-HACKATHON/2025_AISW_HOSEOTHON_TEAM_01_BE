package com._oormthonUNIV.newnew;

import com._oormthonUNIV.newnew.ai.worker.ApiWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewnewApplicationTests {

    @Autowired
    private ApiWorker apiWorker;


	@Test
	void contextLoads() {
        apiWorker.chat("connect test, Say yes");
	}

}
