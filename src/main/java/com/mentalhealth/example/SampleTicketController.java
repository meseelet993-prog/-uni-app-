package com.mentalhealth.example;

import com.mentalhealth.entity.SampleTicket;
import com.mentalhealth.entity.TicketStatus;
import com.mentalhealth.entity.TicketCategory;
import com.mentalhealth.entity.TicketPriority;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/example/tickets")
public class SampleTicketController {
    
    // 示例数据存储
    private List<SampleTicket> tickets = new ArrayList<>();
    private Long idCounter = 1L;
    
    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", tickets);
        return "tickets/list";
    }
    
    @GetMapping("/new")
    public String newTicketForm(Model model) {
        model.addAttribute("ticket", new SampleTicket());
        model.addAttribute("categories", TicketCategory.values());
        model.addAttribute("priorities", TicketPriority.values());
        return "tickets/form";
    }
    
    @PostMapping
    public String createTicket(@ModelAttribute SampleTicket ticket) {
        ticket.setId(idCounter++);
        ticket.setTicketNo("TK" + System.currentTimeMillis());
        ticket.setCreateTime(java.time.LocalDateTime.now());
        ticket.setUpdateTime(java.time.LocalDateTime.now());
        tickets.add(ticket);
        return "redirect:/example/tickets";
    }
    
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        SampleTicket ticket = tickets.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElse(null);
            
        model.addAttribute("ticket", ticket);
        return "tickets/detail";
    }
    
    @GetMapping("/{id}/edit")
    public String editTicketForm(@PathVariable Long id, Model model) {
        SampleTicket ticket = tickets.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElse(null);
            
        model.addAttribute("ticket", ticket);
        model.addAttribute("categories", TicketCategory.values());
        model.addAttribute("priorities", TicketPriority.values());
        model.addAttribute("statuses", TicketStatus.values());
        return "tickets/edit";
    }
    
    @PostMapping("/{id}")
    public String updateTicket(@PathVariable Long id, @ModelAttribute SampleTicket updatedTicket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId().equals(id)) {
                updatedTicket.setId(id);
                updatedTicket.setTicketNo(tickets.get(i).getTicketNo());
                updatedTicket.setCreateTime(tickets.get(i).getCreateTime());
                updatedTicket.setUpdateTime(java.time.LocalDateTime.now());
                tickets.set(i, updatedTicket);
                break;
            }
        }
        return "redirect:/example/tickets/" + id;
    }
}