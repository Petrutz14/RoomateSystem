package com.project.p3project.service;

import com.project.p3project.dao.ApartmentDAO;
import com.project.p3project.dao.UserDAO;
import com.project.p3project.exception.BlankDataException;
import com.project.p3project.model.Apartment;
import com.project.p3project.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    private final ApartmentDAO apartmentDao;
    private final UserDAO userDao;

    public ApartmentService(ApartmentDAO apartmentDao, UserDAO userDao) {
        this.apartmentDao = apartmentDao;
        this.userDao = userDao;
    }

    //Link the creator to the apartment
    public void createWithUser(Apartment apartment, String email) {
        validate(apartment);
        User user = userDao.findByEmail(email);

        Long apartmentId = apartmentDao.save(apartment);
        apartmentDao.addMember(apartmentId, user.getId());
    }

    public Apartment getById(Long id, String email) {
        if (!apartmentDao.isMember(id, email)) {
            throw new IllegalArgumentException("Access Denied: You are not a member of this apartment.");
        }

        Apartment apt = apartmentDao.findById(id);
        if (apt == null) {
            throw new IllegalArgumentException("Apartment not found with id: " + id);
        }
        return apt;
    }

    public List<Apartment> getAllForUser(String email) {
        return apartmentDao.findAllForUser(email);
    }

    public void update(Long id, Apartment apartment, String email) {
        validate(apartment);
        if (!apartmentDao.isMember(id, email)) {
            throw new IllegalArgumentException("You do not have permission to update this apartment.");
        }
        apartmentDao.update(id, apartment);
    }

    public void delete(Long id, String email) {
        if (!apartmentDao.isMember(id, email)) {
            throw new IllegalArgumentException("You do not have permission to delete this.");
        }
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
        if (apartment == null) {
            throw new BlankDataException("Apartment");
        }

        if (apartment.getAddress() == null ||  apartment.getAddress().isBlank()) {
            throw new BlankDataException("Address");
        }
    }
}