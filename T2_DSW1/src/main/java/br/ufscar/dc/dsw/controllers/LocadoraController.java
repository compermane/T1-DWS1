package br.ufscar.dc.dsw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.services.spec.ILocadoraService;
import br.ufscar.dc.dsw.domain.Locadora;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/locadoras")
public class LocadoraController {
    @Autowired
    private ILocadoraService locadoraService;

    // Instancia uma rota GET para o endereço "/"
    @GetMapping("/signUp-locadora")
    public String index(Locadora locadora) {
        return "locadoras/signUp-locadora";
    }

    @PostMapping("/salvar")
    public String cadastrarLocadora(@Valid Locadora locadora, BindingResult result, RedirectAttributes attr) {
        
            // if (result.hasErrors()) {
            //     System.out.println("ROLE: " + locadora.getRole());
            //     System.out.println("[-] Erro durante inserção de locadora");
            //     result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            //     return "locadoras/signUp-locadora";
            // }
        try {
            locadora.setRole("ROLE_LOCADORA");
            locadoraService.salvar(locadora);
        }
        catch(Exception e) {
            System.out.println("[-] Erro ao cadastrar locadora: " + e.getMessage());
            e.printStackTrace();
        }
        
        attr.addFlashAttribute("sucess", "locadora.create.sucess");
        return "redirect:/locadoras/listar";
    }

    @PostMapping("/editar")
    public String editarLocadora(@Valid Locadora locadora, BindingResult result, RedirectAttributes attr) {
        return "index";
    }

}