package coding.test.csv;

import java.io.*;
import java.util.*;
import com.opencsv.CSVWriter;

public class CSVCreate {

    // This creates a random valued data rows and write to a csv file
    public static void writeDataAtOnce(String filePath, Integer rows)
    {

        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);

        try {

            long startTime = System.currentTimeMillis();

            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { "id", "game_no", "game_name", "game_code", "type", "cost_price", "tax", "sale_price", "date_of_sale" });

            String[] gameCodes = {"30142", "216", "963", "4439", "89012"};
            int gameCodeCount = 5;

            Random rand = new Random();
            float costPrice;
            float tax;
            float salePrice;

            // unix timestamp for GMT time
            int startDate = 1711929600; // 1711900800 => 2024/04/01 00:00:00
            int endDate = 1714521599; // 1714521599 => 2024/04/30 23:59:59
            int dateRange = endDate - startDate; // 1714492799 - 1711900800 = 2591999

            for (int i = 0; i < rows; i++) {
                String[] tmpArray = new String[9];
                tmpArray[0] = String.format("%d", i+1);
                tmpArray[1] = String.format("%d", rand.nextInt(100) + 1);
                tmpArray[2] = String.format("%s%d", "Name", i+1);
                tmpArray[3] = gameCodes[rand.nextInt(gameCodeCount)];
                tmpArray[4] = String.format("%d", rand.nextInt(2) + 1);
                costPrice = (float)(rand.nextInt(10000)+1) / 100; // 0.01 ~ 100
                tmpArray[5] = String.format("%.2f", costPrice);
                tax = costPrice * 9 / 100;
                tmpArray[6] = String.format("%.2f", tax);
                salePrice = costPrice + tax;
                tmpArray[7] = String.format("%.2f", salePrice);
                tmpArray[8] = String.format("%d%s", startDate + rand.nextInt(dateRange), "000");
                data.add(tmpArray);
            }

            writer.writeAll(data);

            // closing writer connection
            writer.close();

            long endTime = System.currentTimeMillis();

            System.out.println("Create CSV took " + (endTime - startTime) + " ms") ;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
