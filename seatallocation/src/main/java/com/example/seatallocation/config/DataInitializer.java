package com.example.seatallocation.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.seatallocation.entity.ExamHall;
import com.example.seatallocation.entity.Seating;
import com.example.seatallocation.entity.Student;
import com.example.seatallocation.repository.ExamHallRepository;
import com.example.seatallocation.repository.SeatingRepository;
import com.example.seatallocation.repository.StudentRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final ExamHallRepository examHallRepository;
    private final SeatingRepository seatingRepository;

    public DataInitializer(StudentRepository studentRepository, 
                          ExamHallRepository examHallRepository,
                          SeatingRepository seatingRepository) {
        this.studentRepository = studentRepository;
        this.examHallRepository = examHallRepository;
        this.seatingRepository = seatingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if tables are empty
        if (studentRepository.count() == 0) {
            System.out.println("Initializing sample data...");
            
            // Create students
            studentRepository.save(new Student("101", "Asha Rao", "CSE", "Algorithms"));
            studentRepository.save(new Student("102", "Dev Kumar", "ECE", "Networks"));
            studentRepository.save(new Student("103", "Lina Jose", "CSE", "Databases"));
            studentRepository.save(new Student("104", "Ravi Shah", "ME", "Thermodynamics"));
            studentRepository.save(new Student("105", "Maya Iyer", "ECE", "Signals"));
            
            // Create exam halls
            examHallRepository.save(new ExamHall("H1", 4));
            examHallRepository.save(new ExamHall("H2", 3));
            
            // Create seating allocations
            seatingRepository.save(new Seating("H1", "A1", "101"));
            seatingRepository.save(new Seating("H1", "A2", "102"));
            seatingRepository.save(new Seating("H1", "A3", "103"));
            seatingRepository.save(new Seating("H1", "A4", "104"));
            seatingRepository.save(new Seating("H2", "B1", "105"));
            
            System.out.println("Sample data initialized successfully!");
        }
    }
}
