import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class QuizController {

    private static final String API_URL = "https://opentdb.com/api.php?amount=3&type=multiple"; // Example API URL

    private RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/")
    public String index() {
        return "index";
    }

     @PostMapping("/submit")
    public String submit(String[] userAnswers, Model model) {
        List<Question> questions = fetchQuestionsFromApi();
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i].equalsIgnoreCase(questions.get(i).getAnswer())) {
                score++;
            }
        }
        model.addAttribute("score", score);
        return "result";
    }

    private List<Question> fetchQuestionsFromApi() {
        TriviaResponse response = restTemplate.getForObject(API_URL, TriviaResponse.class);
        return response.getResults();
    }
}