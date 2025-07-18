package com.example.Hotelproject.service;

import com.example.Hotelproject.Repository.Hotelrepo;
import com.example.Hotelproject.model.Hotelcls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class Hotelservice {
    private static Hotelrepo repo;

    public Hotelservice(Hotelrepo repository) {
        this.repo= repository;
    }


    public static Hotelcls addProduct(Hotelcls hotel, MultipartFile file) throws IOException {
        hotel.setFileName (file.getOriginalFilename());
        hotel.setData((file.getBytes()));
        return repo.save(hotel);
    }

    public List<Hotelcls> getAllorder(){
        return repo.findAll();
    }
    public Optional<Hotelcls> findById(Long id) {
        return repo.findById(id); // Assuming repo is a JpaRepository
    }


    public void saveorder(Hotelcls hotel){
        repo.save(hotel);
    }
    public Hotelcls getorderById(Long id){
        return repo.findById(id).orElse(null);
    }
    public void deleteorder(Long id){
        repo.deleteById(id);
    }
}
