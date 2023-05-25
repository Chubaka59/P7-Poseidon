package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.CrudService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class RatingController {
    @Autowired
    private CrudService<Rating> ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model, Principal principal)
    {
        model.addAttribute("username", principal.getName());
        model.addAttribute("ratings", ratingService.getAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        try {
            ratingService.insert(rating);
            model.addAttribute("ratings", ratingService.getAll());
            return "redirect:/rating/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "rating/add";
        }
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("rating", ratingService.getById(id));
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        try {
            ratingService.update(id, rating);
            model.addAttribute("ratings", ratingService.getAll());
            return "redirect:/rating/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "rating/update";
        }
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        try {
            ratingService.delete(id);
            model.addAttribute("ratings", ratingService.getAll());
            return "redirect:/rating/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ratings", ratingService.getAll());
            return "/rating/list";
        }
    }
}
