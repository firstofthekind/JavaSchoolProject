package controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ClientController {
    @GetMapping("D")
    public String getStyledPage(Model model) {
        model.addAttribute("name", "Baeldung Reader");
        return "cssandjs/styledPage";
    }
}
