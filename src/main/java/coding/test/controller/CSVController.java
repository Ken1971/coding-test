package coding.test.controller;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import coding.test.csv.CSVService;
import coding.test.csv.CSVHelper;
import coding.test.message.ResponseMessage;

import coding.test.csv.CSVCreate;

// Controller for creating and importing csv files
@CrossOrigin("http://localhost:8081")
@Controller
public class CSVController {

    @Autowired
    CSVService fileService;

    CSVCreate fileCreate;

    // Import a csv file to mysql database. For task 1 and task 5 use.
    @PostMapping("/import")
    public ResponseEntity<ResponseMessage> importFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                long startTime = System.currentTimeMillis();
                //fileService.save(file);
                fileService.saveToGameSales(file);

                long endTime = System.currentTimeMillis();

                System.out.println("Import CSV took " + (endTime - startTime) + " ms") ;

                message = "Import the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    // for creating csv files with random values. For task 5 use.
    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createFile(@RequestParam("filePath") String filePath, @RequestParam("rows") Integer rows) {
        String message = "";

        try {
            fileCreate.writeDataAtOnce(filePath, rows);

            message = "File created successfully. ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not create the file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }

    }

    // for testing convert date string ot timestamp
    @GetMapping("convert")
    public ResponseEntity<ResponseMessage> convertDateToTimeStamp(@RequestParam("date") String date) {
        String message = "";

        try {

            long timeStamp = CSVHelper.convertToTimestamp(date);

            message = String.format("%s %d", "TimeStamp = ", timeStamp);

            //System.out.println(message);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

    // for testing convert dts to date string
    @GetMapping("dts")
    public ResponseEntity<ResponseMessage> convertDateToTimeStamp(@RequestParam("dts") long dts) {
        String message = "";

        try {

            message = CSVHelper.convertDTSToString(dts);

            //System.out.println(message);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}
