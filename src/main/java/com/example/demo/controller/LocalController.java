package com.example.demo.controller;

import com.example.demo.model.Local;
import com.example.demo.repository.LocalRepository;
import com.example.demo.service.LocalService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/locaux")
public class LocalController {

    @Autowired
    private LocalService localService;

    // Get all locaux
    @GetMapping("/all")
    public ResponseEntity<List<Local>> getAllLocaux() {
        return ResponseEntity.ok(localService.getAllLocaux());
    }

    @GetMapping
    public ResponseEntity<List<Local>> getAvailableLocaux(@RequestParam(value = "disponible", required = false) Boolean disponible) {
        if (disponible != null && disponible) {
            return ResponseEntity.ok(localService.getAvailableLocaux());
        }
        return ResponseEntity.ok(localService.getAllLocaux());
    }

    // Add a new local
    @PostMapping
    public ResponseEntity<Local> addLocal(@RequestBody Local local) {
        Local createdLocal = localService.addLocal(local);
        return ResponseEntity.ok(createdLocal);
    }


    // Update an existing local
    @PutMapping("/{id}")
    public ResponseEntity<Local> updateLocal(
            @PathVariable Long id,
            @RequestBody Local updatedLocal) {
        updatedLocal.setId(id);
        Local local = localService.updateLocal(updatedLocal);
        return ResponseEntity.ok(local);
    }

    // Delete a local
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        localService.deleteLocalById(id);
        return ResponseEntity.noContent().build();
    }

    // Filter locaux (optional query parameter)
    @GetMapping("/search")
    public ResponseEntity<List<Local>> searchLocaux(@RequestParam String query) {
        List<Local> filteredLocaux = localService.searchLocaux(query);
        return ResponseEntity.ok(filteredLocaux);
    }
    // Upload an Excel file and save locals from it
    @PostMapping("/upload")
    public ResponseEntity<String> uploadLocauxFromExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Skip the header row (if it exists)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                if (row != null) {
                    String name = row.getCell(0).getStringCellValue();
                    int size = (int) row.getCell(1).getNumericCellValue();
                    String type = row.getCell(2).getStringCellValue();

                    // Create and save the new Local object
                    Local newLocal = new Local(name, size, type);
                    localService.addLocal(newLocal); // Save to the database
                }
            }

            workbook.close();
            return ResponseEntity.ok("File uploaded successfully and locals saved.");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process the file.");
        }
    }

    @Autowired
    private LocalRepository localRepository;

    @PostMapping("/bulk-create")
    public ResponseEntity<List<Local>> createLocaux(@RequestBody List<Local> locaux) {
        List<Local> savedLocaux = localRepository.saveAll(locaux);
        return ResponseEntity.ok(savedLocaux);
    }

}
