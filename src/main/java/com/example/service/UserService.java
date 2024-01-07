package com.example.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.example.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private List<User> users;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        try {
            this.users = loadUsers();
        } catch (IOException | CsvException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    private List<User> loadUsers() throws IOException, CsvException {
        File file = ResourceUtils.getFile("classpath:users.csv");

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> lines = reader.readAll();
            lines = lines.subList(1, lines.size());

            List<User> users = new ArrayList<>();
            for (String[] line : lines) {
                String username = line[0];
                String password = passwordEncoder.encode(line[1]); 
                users.add(new User(username, password));
            }

            return users;
        }
    }

    public boolean isValidUser(String username, String password) {
        Optional<User> user = users.stream()
        .filter(u -> u.getUsername().equals(username)) // Use 'u' instead of 'user'
        .findFirst();
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}
