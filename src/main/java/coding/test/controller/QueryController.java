package coding.test.controller;

import java.util.Arrays;
import java.util.List;

import coding.test.message.ResponseMessage;
import coding.test.model.Game_Sales;
import coding.test.repository.Game_SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import coding.test.csv.CSVHelper;

@Controller
public class QueryController {
    @Autowired
    private Game_SalesRepository gameSalesRepository;

    // For task 3 use. Use date range in unix timestamp.
    @GetMapping(path="/getGameSales", params = {"page", "salePrice", "fromDTS", "toDTS", "moreThanPrice"})
    public @ResponseBody ResponseEntity<Page<Game_Sales>> getGameSales(@RequestParam("salePrice") double salePrice, @RequestParam("moreThanPrice") int moreThanPrice, @RequestParam("fromDTS") long fromDTS, @RequestParam("toDTS") long toDTS, @RequestParam("page") int page) {

        long startTime = System.currentTimeMillis();

        int pageSize = 100;
        Pageable paging = PageRequest.of(page, pageSize);
        Page<Game_Sales> res = null;

        try {
            if (moreThanPrice == 1) {
                res = gameSalesRepository.findAllByGreaterSalePrice(salePrice, fromDTS, toDTS, paging);
            } else {
                res = gameSalesRepository.findAllByLessSalePrice(salePrice, fromDTS, toDTS, paging);
            }
            //Page<Game_Sales> res = gameSalesRepository.findAll(game_no, paging);
            long endTime = System.currentTimeMillis();
            System.out.println("getGameSales " + (endTime - startTime) + " ms") ;

            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
            System.out.println("getGameSales failed. " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    // For task 3 use. Use date range in string format like '2024-04-01 00:00:00' ('yyyy-mm-dd hh:mm:ss')
    @GetMapping(path="/getGameSales", params = {"page", "salePrice", "fromDate", "toDate", "moreThanPrice"})
    public @ResponseBody ResponseEntity<Page<Game_Sales>> getGameSales(@RequestParam("salePrice") double salePrice, @RequestParam("moreThanPrice") int moreThanPrice, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, @RequestParam("page") int page) {

        long startTime = System.currentTimeMillis();

        int pageSize = 100;
        Pageable paging = PageRequest.of(page, pageSize);
        Page<Game_Sales> res = null;

        try {
            long fromDTS = CSVHelper.convertToTimestamp(fromDate);
            long toDTS = CSVHelper.convertToTimestamp(toDate);

            if (moreThanPrice == 1) {
                res = gameSalesRepository.findAllByGreaterSalePrice(salePrice, fromDTS, toDTS, paging);
            } else {
                res = gameSalesRepository.findAllByLessSalePrice(salePrice, fromDTS, toDTS, paging);
            }
            //Page<Game_Sales> res = gameSalesRepository.findAll(game_no, paging);
            long endTime = System.currentTimeMillis();
            System.out.println("getGameSales " + (endTime - startTime) + " ms") ;

            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            //return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
            System.out.println("getGameSales failed. " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    // For task 4 use. Use unix timestamp for date range search.
    @GetMapping(path="/getTotalSales", params = {"fromDTS", "toDTS"})
    public @ResponseBody ResponseEntity<ResponseMessage> getTotalSales(@RequestParam("fromDTS") long fromDTS, @RequestParam("toDTS") long toDTS) {

        String message = "";
        long startTime = System.currentTimeMillis();

        try {
            String resStr = gameSalesRepository.findGameSold(fromDTS, toDTS);
            List<String> resItems = Arrays.asList(resStr.split("\\s*,\\s*"));
            message = String.format("Total game sold: %s. Total Sale Price: %s ", resItems.getFirst(), resItems.get(1));

            long endTime = System.currentTimeMillis();
            System.out.println("getTotalSales " + (endTime - startTime) + " ms") ;

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not get total sales. " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    // For task 4 use. Search by date range in string format like '2024-04-01 00:00:00' ('yyyy-mm-dd hh:mm:ss')
    @GetMapping(path="/getTotalSales", params = {"fromDate", "toDate"})
    public @ResponseBody ResponseEntity<ResponseMessage> getTotalSales(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {

        String message = "";
        long startTime = System.currentTimeMillis();
        long fromDTS = CSVHelper.convertToTimestamp(fromDate);
        long toDTS = CSVHelper.convertToTimestamp(toDate);

        System.out.println(String.format("fromDTS = %d, toDTS = %d", fromDTS, toDTS));

        try {
            String resStr = gameSalesRepository.findGameSold(fromDTS, toDTS);
            List<String> resItems = Arrays.asList(resStr.split("\\s*,\\s*"));
            message = String.format("Total game sold: %s. Total Sale Price: %s ", resItems.getFirst(), resItems.get(1));

            long endTime = System.currentTimeMillis();
            System.out.println("getTotalSales " + (endTime - startTime) + " ms") ;

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not get total sales. " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    // For task 4 use. Search by unix time stamp and game_no.
    @GetMapping(path="/getTotalSales", params = {"gameNo", "fromDTS", "toDTS"})
    public @ResponseBody ResponseEntity<ResponseMessage> getTotalSales(@RequestParam("gameNo") String gameNo, @RequestParam("fromDTS") long fromDTS, @RequestParam("toDTS") long toDTS) {

        String message = "";
        long startTime = System.currentTimeMillis();

        try {
            String resStr = gameSalesRepository.findGameSoldWithGameCode(gameNo, fromDTS, toDTS);
            List<String> resItems = Arrays.asList(resStr.split("\\s*,\\s*"));
            message = String.format("Game Code: %s. Total game sold: %s. Total Sale Price: %s ", gameNo, resItems.getFirst(), resItems.get(1));

            long endTime = System.currentTimeMillis();
            System.out.println("getTotalSales " + (endTime - startTime) + " ms") ;

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not get total sales. " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    // For task 4 use. Search by date range string and game_no.
    @GetMapping(path="/getTotalSales", params = {"gameNo", "fromDate", "toDate"})
    public @ResponseBody ResponseEntity<ResponseMessage> getTotalSales(@RequestParam("gameNo") String gameNo, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
        // This returns a JSON or XML with the users
        //return bookRepository.findAll();

        String message = "";
        long startTime = System.currentTimeMillis();
        long fromDTS = CSVHelper.convertToTimestamp(fromDate);
        long toDTS = CSVHelper.convertToTimestamp(toDate);

        try {
            String resStr = gameSalesRepository.findGameSoldWithGameCode(gameNo, fromDTS, toDTS);
            List<String> resItems = Arrays.asList(resStr.split("\\s*,\\s*"));
            message = String.format("Game Code: %s. Total game sold: %s. Total Sale Price: %s ", gameNo, resItems.getFirst(), resItems.get(1));

            long endTime = System.currentTimeMillis();
            System.out.println("getTotalSales " + (endTime - startTime) + " ms") ;

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not get total sales. " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
}
