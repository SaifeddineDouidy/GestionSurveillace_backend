package com.example.demo.service;

import com.example.demo.model.Holiday;
import com.example.demo.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    // Fetch all holidays
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    // Fetch holidays within a specific range
    public List<Holiday> getHolidaysInRange(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByDateBetween(startDate, endDate);
    }

    // Add a new holiday
    public Holiday addHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    // Add multiple holidays (e.g., for loading Moroccan holidays)
    public List<Holiday> addHolidays(List<Holiday> holidays) {
        return holidayRepository.saveAll(holidays);
    }

    // Load predefined Moroccan holidays
    public void loadMoroccanHolidays() {
        List<Holiday> moroccanHolidays = List.of(
                new Holiday(null, "New Year's Day", LocalDate.of(LocalDate.now().getYear(), 1, 1)),
                new Holiday(null, "Labour Day", LocalDate.of(LocalDate.now().getYear(), 5, 1)),
                new Holiday(null, "Throne Day", LocalDate.of(LocalDate.now().getYear(), 7, 30)),
                new Holiday(null, "Revolution Day", LocalDate.of(LocalDate.now().getYear(), 8, 20)),
                new Holiday(null, "Independence Day", LocalDate.of(LocalDate.now().getYear(), 11, 18))
        );
        addHolidays(moroccanHolidays);
    }
}
