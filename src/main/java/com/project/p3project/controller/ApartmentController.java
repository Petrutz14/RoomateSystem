package com.project.p3project.controller;

import com.project.p3project.model.Apartment;
import com.project.p3project.service.ApartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    private final ApartmentService apartmentService;

    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping
    public void create(@RequestBody Apartment apartment) {
        apartmentService.create(apartment);
    }

    @GetMapping("/{id}")
    public Apartment getById(@PathVariable Long id) {
        return apartmentService.getById(id);
    }

    @GetMapping
    public List<Apartment> getAll() {
        return apartmentService.getAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Apartment apartment) {
        apartmentService.update(id, apartment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        apartmentService.delete(id);
    }

    @PostMapping("/{apartmentId}/members/{userId}")
    public void addMember(@PathVariable Long apartmentId, @PathVariable Long userId) {
        apartmentService.addMember(apartmentId, userId);
    }
}