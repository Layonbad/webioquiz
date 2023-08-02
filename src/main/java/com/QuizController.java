package com;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class QuizController {

    private static final String API_URL = "https://opentdb.com/api.php?amount=10";

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String index(Model model) {
        List<Question> questions = fetchQuestionsFromApi();
        model.addAttribute("questions", questions); // Add questions to the model
        return "index";
    }

    @PostMapping("/submit")
    public String submit(Question[] questions, Model model) {
        int score = 0;

        for (Question question : questions) {
            if (question.getChosenAnswer().equalsIgnoreCase(question.getCorrectAnswer())) {
                score++;
            }
        }

        model.addAttribute("score", score);
        return "result";
    }

    private List<Question> fetchQuestionsFromApi() {
        ResponseEntity<TriviaResponse> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, null,
                TriviaResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            TriviaResponse response = responseEntity.getBody();
            if (response != null) {
                return response.getResults();
            }
        }
        throw new RuntimeException("Failed to fetch questions from API");
    }
}