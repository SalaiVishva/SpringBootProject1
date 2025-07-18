package com.example.Hotelproject.control;

import com.example.Hotelproject.model.Hotelcls;
import com.example.Hotelproject.service.Hotelservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class HotelController {

    @Autowired
    private Hotelservice hotelservice;
    // Directory for saving uploaded files
    private static final String UPLOAD_DIR = "uploads/";


    @GetMapping("/")
    public String viewHomepage(Model model){
        model.addAttribute("hotels",hotelservice.getAllorder());
        return "index";

    }
    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("hotel",new Hotelcls());
        return "new_order";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("hotel") Hotelcls hotel,@RequestParam(value = "file", required = false) MultipartFile file,Model model){
        try {
            if (file != null && !file.isEmpty()) {
                // Save the file to the UPLOAD_DIR and store the file name
                String fileName = file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                hotel.setFileName(fileName);
            }
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload image.");
            return "index";
        }

        hotelservice.saveorder(hotel);
        return "redirect:/";
    }
    @GetMapping("/image/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            if (!Files.exists(filePath)) {
                System.err.println("File not found: " + filePath);
                return ResponseEntity.notFound().build();
            }
            byte[] imageBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG depending on the file type
                    .body(imageBytes);
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Hotelcls hotel=hotelservice.getorderById(id);
        model.addAttribute("hotel",hotel);
        return "edit_order";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
                             @ModelAttribute("hotel") Hotelcls hotel,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             Model model) {

        // Check if hotel/product exists by ID
        Optional<Hotelcls> existingHotel = hotelservice.findById(id);

        if (existingHotel.isPresent()) {
            hotel.setId(id); // Ensure correct ID is set

            try {
                if (file != null && !file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    Path uploadPath = Paths.get(UPLOAD_DIR);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    Path filePath = uploadPath.resolve(fileName);
                    file.transferTo(filePath.toFile());

                    hotel.setFileName(fileName);
                } else {
                    // If no new file, retain the existing filename
                    hotel.setFileName(existingHotel.get().getFileName());
                }
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload image.");
                return "edit_order";
            }

            // Save updated product
            hotelservice.saveorder(hotel);
            return "redirect:/";
        } else {
            // If no such product, show all products
            List<Hotelcls> allHotels = hotelservice.getAllorder();
            model.addAttribute("hotels", allHotels);
            model.addAttribute("error", "Product not found.");
            return "index"; // Replace with your actual view name
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id){
        hotelservice.deleteorder(id);
        return "redirect:/";
    }
}
