package com.example.demo.service;

import com.example.demo.model.Departement;
import com.example.demo.model.Option;
import com.example.demo.model.Module;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class OptionsModuleFileService {

    @Autowired
    private DepartementService departementService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private ModuleService moduleService;

    public Map<String, Object> processExcelFile(MultipartFile file) throws IOException {
        Map<String, Object> result = new HashMap<>();
        int totalRows = 0;
        int processedRows = 0;
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Maps to keep track of existing entities
            Map<String, Departement> departmentMap = new HashMap<>();
            Map<String, Option> optionMap = new HashMap<>();

            // Skip header row
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                totalRows++;

                try {
                    String departmentName = getCellValueAsString(row.getCell(0));
                    String optionName = getCellValueAsString(row.getCell(1));
                    String year = getCellValueAsString(row.getCell(2));
                    int numberOfStudents = (int) row.getCell(3).getNumericCellValue();
                    String moduleName = getCellValueAsString(row.getCell(4));

                    if (departmentName.isEmpty() || optionName.isEmpty() || moduleName.isEmpty()) {
                        errors.add("Row " + row.getRowNum() + ": Missing required data");
                        continue;
                    }

                    // Get or create department
                    Departement department = departmentMap.computeIfAbsent(departmentName, name -> {
                        return departementService.findByDepartmentName(name)
                                .orElseGet(() -> {
                                    Departement newDept = new Departement();
                                    newDept.setDepartmentName(name);
                                    return departementService.saveDepartement(newDept);
                                });
                    });

                    // Get or create option
                    String optionKey = optionName + "-" + department.getId();
                    Option option = optionMap.computeIfAbsent(optionKey, k -> {
                        Option existingOption = optionService.findByNomDeFiliereAndDepartement(optionName, department);
                        if (existingOption != null) {
                            return existingOption;
                        }
                        Option newOption = new Option();
                        newOption.setNomDeFiliere(optionName);
                        newOption.setAnnee(year);
                        newOption.setNbrInscrit(numberOfStudents);
                        newOption.setDepartement(department);
                        return optionService.addOptions(newOption);
                    });

                    // Create module if it doesn't exist
                    if (!moduleService.existsByNomModuleAndOption(moduleName, option)) {
                        Module module = new Module();
                        module.setNomModule(moduleName);
                        module.setOption(option);
                        moduleService.addModule(module);
                    }

                    processedRows++;
                } catch (Exception e) {
                    errors.add("Row " + row.getRowNum() + ": " + e.getMessage());
                }
            }
        }

        result.put("totalRows", totalRows);
        result.put("processedRows", processedRows);
        result.put("errors", errors);
        return result;
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}