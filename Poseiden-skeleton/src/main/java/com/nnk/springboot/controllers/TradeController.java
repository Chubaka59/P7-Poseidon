package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
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
public class TradeController {
    @Autowired
    private CrudService<Trade> tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model, Principal principal)
    {
        model.addAttribute("username", principal.getName());
        model.addAttribute("trades", tradeService.getAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTrade(Trade trade) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        try {
            tradeService.insert(trade);
            model.addAttribute("trades", tradeService.getAll());
            return "redirect:/trade/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "trade/add";
        }
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("trade", tradeService.getById(id));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        try {
            tradeService.update(id, trade);
            model.addAttribute("trades", tradeService.getAll());
            return "redirect:/trade/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "trade/update";
        }
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        try {
            tradeService.delete(id);
            model.addAttribute("trades", tradeService.getAll());
            return "redirect:/trade/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("trades", tradeService.getAll());
            return "/trade/list";
        }
    }
}
