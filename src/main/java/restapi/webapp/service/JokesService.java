package restapi.webapp.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import restapi.webapp.dto.ApiJokesDTO;
import restapi.webapp.dto.JokeDTO;
import restapi.webapp.pojos.Joke;

import java.util.concurrent.CompletableFuture;

@Service
public class JokesService {

    private final RestTemplate template;

    public JokesService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<ApiJokesDTO> joke(){
        String urlTemplate = String.format("https://v2.jokeapi.dev/joke/Any?type=single");
        ApiJokesDTO aJoke = this.template.getForObject(urlTemplate,ApiJokesDTO.class);
        return CompletableFuture.completedFuture(aJoke);

    }

    public Joke parsedJoke(ApiJokesDTO apiJoke){
        Joke joke = new Joke(apiJoke.getJoke(), apiJoke.getCategory());
        return joke;
    }

}
