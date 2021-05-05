package service;

import com.horacio.mutant.repository.DnaModel;
import com.horacio.mutant.repository.DnaRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
/*
@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@AutoConfigureWebTestClient
public class IntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void words() throws Exception {
        client.post().uri("/").body(Mono.just("foo"), String.class)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo("FOO");
    }

}*/


