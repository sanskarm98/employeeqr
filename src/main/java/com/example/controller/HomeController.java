package com.example.controller;

import com.example.model.UserInfo;
import com.example.repository.UserInfoRepository;
import com.google.zxing.Reader;
import com.google.zxing.WriterException;
import com.opencsv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVRecord;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.qrcode.QRCodeGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private QRCodeGenerator qrCodeGenerator; // Inject the QR code generator

    @GetMapping("/home")
    public String showHomePage(@RequestParam(required = false) String id, Model model) {
        List<UserInfo> userInfoList;
        if (id != null) {
            userInfoList = userInfoRepository.findById(id);
        } else {
            userInfoList = userInfoRepository.findAll();
        }

        model.addAttribute("userInfoList", userInfoList);
        return "home";
    }

    @GetMapping("/generateQRCode")
    public byte[] generateQRCode(@RequestParam String data) throws WriterException, IOException {
        return qrCodeGenerator.generateQRCodeImage(data); // Call the QR code generator
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<UserInfo> userInfoList = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);


            
for (CSVRecord csvRecord : csvParser) {
                Long id = Long.parseLong(csvRecord.get(0));
                String name = csvRecord.get(1);
                String city = csvRecord.get(2);
                String number = csvRecord.get(3);
                UserInfo userInfo = new UserInfo(id, name, city, number);
                userInfoList.add(userInfo);
            }

            // Save data to database
            userInfoRepository.saveAll(userInfoList);
            model.addAttribute("message", "CSV data uploaded successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error uploading CSV: " + e.getMessage());
        }

        // Refresh the list of users
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        model.addAttribute("userInfoList", userInfoList);
        return "home";
    }
}
