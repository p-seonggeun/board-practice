package hello.practice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/")
    public String mainPage() {
        return "Hello Main Page";
    }

    @GetMapping("/user")
    public String userPage() {
        return "Hello User Page";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "Hello Admin Page";
    }
}
