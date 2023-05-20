package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
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
public class BidListController {
    @Autowired
    private CrudService<BidList> bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model, Principal principal)
    {
        model.addAttribute("username", principal.getName());
        model.addAttribute("bidlists", bidListService.getAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                bidListService.insert(bid);
                model.addAttribute("bidlists", bidListService.getAll());
                return "redirect:/bidList/list";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("bidList", bidListService.getById(id));
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                bidListService.update(id, bidList);
                model.addAttribute("bidLists", bidListService.getAll());
                return "redirect:/bidList/list";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "bidList/update";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        try {
            bidListService.delete(id);
            model.addAttribute("bidlists", bidListService.getAll());
            return "redirect:/bidList/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bidlists", bidListService.getAll());
            return "/bidList/list";
        }
    }
}
