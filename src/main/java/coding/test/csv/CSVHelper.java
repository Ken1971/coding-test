package coding.test.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.lang.Double;

import coding.test.model.Game_Sales;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] GAMEHEADERS = {"id", "game_no", "game_name", "game_code", "type", "cost_price", "tax", "sale_price", "date_of_sale" };

    public static class EntityManagerFactoryHelper {

        private static EntityManagerFactory factory;

        static {
            try {
                // Set up factory right here
                factory = Persistence.createEntityManagerFactory("jpa-example");
            } catch(ExceptionInInitializerError e) {
                throw e;
            }
        }

        public static EntityManagerFactory getFactory() {
            return factory;
        }

        public static EntityManager getEntityManager() {
            return factory.createEntityManager();
        }

        public static void shutDown() {
            factory.close();
        }

    }

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());

    }

    // convert a date string to unix timestamp
    public static long convertToTimestamp(String date) {

        try {
            Instant instant = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.of("GMT"))
                    .toInstant();
            long actualTimestamp = instant.toEpochMilli();
            return actualTimestamp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Fail to convert to timestamp: " + e.getMessage() + ". Format: yyyy-MM-dd HH:mm:ss");
        }
    }

    // convert a unix timestamp back to date string
    public static String convertDTSToString(long dts) {

        try {
            Date date = new Date(dts);
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String java_date = jdf.format(date);
            return java_date;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Fail to convert from DTS: " + e.getMessage());
        }
    }

    // load all data from a csv file in game_sales table format
    public static List<Game_Sales> csvToGameSales(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Game_Sales> gameSales = new ArrayList<Game_Sales>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Game_Sales gameSale = new Game_Sales(
                        Long.parseLong(csvRecord.get(GAMEHEADERS[0])),
                        //0L,
                        Integer.parseInt(csvRecord.get(GAMEHEADERS[1])),
                        csvRecord.get(GAMEHEADERS[2]),
                        csvRecord.get(GAMEHEADERS[3]),
                        Integer.parseInt(csvRecord.get(GAMEHEADERS[4])),
                        BigDecimal.valueOf(Double.parseDouble(csvRecord.get(GAMEHEADERS[5]))),
                        BigDecimal.valueOf(Double.parseDouble(csvRecord.get(GAMEHEADERS[6]))),
                        BigDecimal.valueOf(Double.parseDouble(csvRecord.get(GAMEHEADERS[7]))),
                        Long.parseLong(csvRecord.get(GAMEHEADERS[8]))
                );

                gameSales.add(gameSale);

            }

            return gameSales;
        } catch (Exception e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
