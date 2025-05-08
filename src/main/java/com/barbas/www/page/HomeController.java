package com.barbas.www.page;

import com.barbas.core.config.Config;
import com.barbas.core.model.Service;
import com.barbas.core.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Config config;
    private final ServiceRepository serviceRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Service> services = serviceRepository.findAllByStatusOrderByTitle(Service.Status.ON);
        model.addAttribute("title", config.getName());
        model.addAttribute("services", services);
        return "home";
    }
}