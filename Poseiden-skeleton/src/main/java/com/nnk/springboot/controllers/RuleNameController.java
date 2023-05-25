package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
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
public class RuleNameController {
    @Autowired
    private CrudService<RuleName> ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model, Principal principal)
    {
        model.addAttribute("username", principal.getName());
        model.addAttribute("ruleNames", ruleNameService.getAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        try {
            ruleNameService.insert(ruleName);
            model.addAttribute("ruleNames", ruleNameService.getAll());
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "ruleName/add";
        }
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("ruleName", ruleNameService.getById(id));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        try {
            ruleNameService.update(id, ruleName);
            model.addAttribute("ruleName", ruleNameService.getAll());
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "ruleName/update";
        }
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        try {
            ruleNameService.delete(id);
            model.addAttribute("ruleNames", ruleNameService.getAll());
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ruleNames", ruleNameService.getAll());
            return "/ruleName/list";
        }
    }
}
