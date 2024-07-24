package coding.test.csv;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import coding.test.model.Game_Sales;
import coding.test.repository.Game_SalesRepository;

@Service
public class CSVService {
    @Autowired
    Game_SalesRepository repo_game;

    // import a csv file and save to game_sales
    public void saveToGameSales(MultipartFile file) {
        try {
            long startTime = System.currentTimeMillis();

            List<Game_Sales> gameSales = CSVHelper.csvToGameSales(file.getInputStream());

            long endTime = System.currentTimeMillis();
            System.out.println("parse csv time: " + (endTime - startTime) + " ms") ;

            repo_game.saveAll(gameSales);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

}
