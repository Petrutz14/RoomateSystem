package com.project.p3project.service;

import com.project.p3project.dao.ApartmentDao;
import com.project.p3project.dao.UserDao;
import com.project.p3project.exception.BlankDataException;
import com.project.p3project.model.Apartment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    private final ApartmentDao apartmentDao;
    private final UserDao userDao;

    public ApartmentService(ApartmentDao apartmentDao, UserDao userDao) {
        this.apartmentDao = apartmentDao;
        this.userDao = userDao;
    }

    public void create(Apartment apartment) {
        validate(apartment);
        apartmentDao.save(apartment);
    }

    public Apartment getById(Long id) {
        Apartment apt = apartmentDao.findById(id);
        if (apt == null) {
            throw new IllegalArgumentException("Apartment not found with id: " + id);
        }
        return apt;
    }

    public List<Apartment> getAll() {
        return apartmentDao.findAll();
    }

    public void update(Long id, Apartment apartment) {
        validate(apartment);
        if (apartmentDao.findById(id) == null) {
            throw new IllegalArgumentException("Apartment not found with id: " + id);
        }
        apartmentDao.update(id, apartment);
    }

    public void delete(Long id) {
        int rows = apartmentDao.deleteById(id);
        if (rows == 0) {
            throw new IllegalArgumentException("Apartment not found with id: " + id);
        }
    }

    public void addMember(Long apartmentId, Long userId) {
        if (apartmentDao.findById(apartmentId) == null) {
            throw new IllegalArgumentException("Apartment does not exist");
        }
        if (userDao.findById(userId) == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        apartmentDao.addMember(apartmentId, userId);
    }

    public void removeMember(Long apartmentId, Long userId) {
        if (apartmentDao.findById(apartmentId) == null) {
            throw new IllegalArgumentException("Apartment does not exist");
        }
        apartmentDao.removeMember(apartmentId, userId);
    }

    public void validate(Apartment apartment) {
        if (apartment.getAddress() == null ||  apartment.getAddress().isBlank()) {
            throw new BlankDataException("Address");
        }

        if (apartment == null) {
            throw new BlankDataException("Apartment");
        }
    }
}