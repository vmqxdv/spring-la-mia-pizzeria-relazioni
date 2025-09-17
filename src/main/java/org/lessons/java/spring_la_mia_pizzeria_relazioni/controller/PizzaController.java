package org.lessons.java.spring_la_mia_pizzeria_relazioni.controller;

import java.util.List;
import java.util.Optional;

import org.lessons.java.spring_la_mia_pizzeria_relazioni.model.Pizza;
import org.lessons.java.spring_la_mia_pizzeria_relazioni.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

  @Autowired
  private PizzaRepository repository;

  @GetMapping
  public String index(@RequestParam(name = "name", required = false) String name, Model model) {
    List<Pizza> pizzas;
    if (name != null && !name.isBlank()) {
      pizzas = repository.findByNameContainingIgnoreCase(name);
    } else
      pizzas = repository.findAll();

    model.addAttribute("pizzas", pizzas);
    return "pizzas/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable("id") Integer id, Model model) {
    Optional<Pizza> pizza = repository.findById(id);
    model.addAttribute("pizza", pizza.orElse(null));
    return "pizzas/show";
  }

  @GetMapping("/create")
  public String create(Model model) {
    model.addAttribute("pizza", new Pizza());

    return "pizzas/create";
  }

  @PostMapping("/create")
  public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors())
      return "pizzas/create";

    repository.save(formPizza);

    return "redirect:/pizzas";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("pizza", repository.findById(id).get());

    return "pizzas/edit";
  }

  @PostMapping("/edit/{id}")
  public String update(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors())
      return "pizzas/edit";

    repository.save(formPizza);

    return "redirect:/pizzas";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Integer id) {
    repository.deleteById(id);

    return "redirect:/pizzas";
  }
}
