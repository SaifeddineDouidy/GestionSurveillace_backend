package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Departement;
import com.example.demo.model.Enseignant;
import com.example.demo.model.Option;
import com.example.demo.service.DepartementService;
import com.example.demo.service.EnseignantService;
import com.example.demo.service.OptionService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private EnseignantService enseignantService;

    @GetMapping
    public List<Departement> getAllDepartements() {
        return departementService.getAllDepartements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Long id) {
        return departementService.getDepartementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<List<Departement>> createDepartments(@RequestBody List<Departement> departments) {
        List<Departement> savedDepartments = departementService.saveAll(departments);
        return ResponseEntity.ok(savedDepartments);
    }

    @PostMapping
    public Departement createDepartement(@RequestBody Departement departement) {
        return departementService.saveDepartement(departement);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Departement> updateDepartement(@PathVariable Long id, @RequestBody Departement departementDetails) {
        return departementService.getDepartementById(id).map(departement -> {

            if (departementDetails.getDepartmentName() != null) {
                departement.setDepartmentName(departementDetails.getDepartmentName());
            }

                for (Enseignant enseignant : departement.getEnseignants()) {
                    enseignant.setDepartment(departement);
                }

            Departement updatedDepartement = departementService.saveDepartement(departement);
            return ResponseEntity.ok(updatedDepartement);
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        if (departementService.getDepartementById(id).isPresent()) {
            departementService.deleteDepartement(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/enseignants")
    public ResponseEntity<List<Enseignant>> getEnseignantsByDepartement(@PathVariable Long id) {
        try {
            List<Enseignant> enseignants = departementService.getEnseignantsByDepartement(id);
            return ResponseEntity.ok(enseignants);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/options")
    public ResponseEntity<List<Option>> getOptionsByDepartment(@PathVariable Long id) {
        List<Option> options = optionService.getOptionsByDepartment(id);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDepartmentsFromFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.badRequest().body("Invalid file name.");
        }

        try {
            if (fileName.endsWith(".csv")) {
                return handleCsvFile(file);
            } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                return handleExcelFile(file);
            } else {
                return ResponseEntity.badRequest().body("Unsupported file type. Please upload .csv, .xls, or .xlsx files.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the file: " + e.getMessage());
        }
    }

    private ResponseEntity<String> handleCsvFile(MultipartFile file) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                if (lineCount++ == 0) continue;

                String[] fields = line.split(",");
                if (fields.length < 4) {
                    return ResponseEntity.badRequest().body("Invalid CSV format.");
                }

                String departmentName = fields[0].trim();
                String teacherName = fields[1].trim();
                String teacherEmail = fields[2].trim();
                boolean dispense = Boolean.parseBoolean(fields[3].trim());

                saveDepartementAndEnseignant(departmentName, teacherName, teacherEmail, dispense);
            }
            return ResponseEntity.ok("CSV file uploaded and data saved successfully.");
        }
    }

    private ResponseEntity<String> handleExcelFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            var workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String departmentName = row.getCell(0).getStringCellValue().trim();
                String teacherName = row.getCell(1).getStringCellValue().trim();
                String teacherEmail = row.getCell(2).getStringCellValue().trim();
                boolean dispense = row.getCell(3).getBooleanCellValue();

                saveDepartementAndEnseignant(departmentName, teacherName, teacherEmail, dispense);
            }
            return ResponseEntity.ok("Excel file uploaded and data saved successfully.");
        }
    }

    private void saveDepartementAndEnseignant(String departmentName, String teacherName, String teacherEmail, boolean dispense) {

        Departement department = departementService.findByName(departmentName)
                .orElseGet(() -> {
                    Departement newDepartment = new Departement();
                    newDepartment.setDepartmentName(departmentName);
                    return departementService.saveDepartement(newDepartment);
                });

        Enseignant teacher = new Enseignant();
        teacher.setName(teacherName);
        teacher.setEmail(teacherEmail);
        teacher.setDispense(dispense);
        teacher.setDepartment(department);

        enseignantService.saveEnseignant(teacher);
    }

}
