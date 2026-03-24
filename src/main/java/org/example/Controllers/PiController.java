package org.example.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PiController {
    @GetMapping("/pi")
    public String getPi() {
        return "Pi: " + Math.PI;
    }
}
