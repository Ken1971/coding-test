package coding.test.repository;

import coding.test.csv.CSVHelper;
import coding.test.model.Game_Sales;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsertSalesRepoClass implements ListCrudRepository<Game_Sales, Integer> {

    EntityManager entityManager = CSVHelper.EntityManagerFactoryHelper.getEntityManager();

    private StringBuilder insertHeaders() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into game_sales("
                + "id, game_no, game_name, game_code, type, cost_price, tax, sale_price, date_of_sale) values");
        return sb;
    }

    @Override
    public <S extends Game_Sales> List<S> saveAll(Iterable<S> gameList) {
        try {
            System.out.println("here");
            List<S> gsList = new ArrayList<>();

            StringBuilder sb = insertHeaders();
            int recCount = 0;
            for (Object i : gameList) {
                recCount += 1;
            }
            System.out.println("total records: " + recCount);
            int cnt = 0;
            int batchInsertAmt = 1000; // number of rows before mass insert to database
            int progressIndex = 0;
            long unixTime = System.currentTimeMillis();

            // init the progress table
            String sql = String.format("insert into import_progress(id, total_records, imported_records, start_dts, end_dts) " +
                    "values(1, %d, 0, %d, 0) on duplicate key update total_records = %d, imported_records = 0, start_dts = %d," +
                    "end_dts = 0;", recCount, unixTime, recCount, unixTime);
            entityManager.getTransaction().begin();
            Query qry1 = entityManager.createNativeQuery(sql);
            qry1.executeUpdate();
            entityManager.getTransaction().commit();

            String sqlUpdate = "update import_progress set imported_records = %d where id = 1";
            String sqlFinal = "update import_progress set end_dts = %d where id = 1";

            for (Game_Sales sales : gameList) {

                cnt += 1;
                if (cnt > 1) {
                    sb.append(", ");
                }
                sb.append(String.format("('%d','%d','%s','%s','%d','%f','%f','%f','%d')",
                        sales.getId(), sales.getGame_no(), sales.getGame_name(), sales.getGame_code(),
                        sales.getType(), sales.getCost_price(), sales.getTax(), sales.getSale_price(), sales.getDate_of_sale()));
                //gsList.add((S)sales); not needed. return empty list is fine
                if (cnt >= batchInsertAmt) {
                    progressIndex += cnt;
                    System.out.println("insert progress " + progressIndex);
                    sb.append(";");

                    // @Transactional doesn't work. Keep getting Executing an update/delete query error
                    // Have to do begin and commit manually to make it work
                    entityManager.getTransaction().begin();
                    Query query = entityManager.createNativeQuery(sb.toString());
                    // update progress table
                    Query qry2 = entityManager.createNativeQuery(String.format(sqlUpdate, progressIndex));
                    //entityManager.createNativeQuery(sb.toString()).executeUpdate();
                    query.executeUpdate();
                    qry2.executeUpdate();
                    entityManager.getTransaction().commit();
                    cnt = 0;
                    sb = insertHeaders();
                }
            }
            if (cnt > 0) { // insert the left-over records
                progressIndex += cnt;
                System.out.println("insert progress " + progressIndex);
                sb.append(";");
                entityManager.getTransaction().begin();
                Query query = entityManager.createNativeQuery(sb.toString());
                // update progress table
                Query qry2 = entityManager.createNativeQuery(String.format(sqlUpdate, progressIndex));
                query.executeUpdate();
                qry2.executeUpdate();
                entityManager.getTransaction().commit();
            }

            long endTime = System.currentTimeMillis();

            // finalize the progress table
            entityManager.getTransaction().begin();
            Query qry3 = entityManager.createNativeQuery(String.format(sqlFinal, endTime));
            qry3.executeUpdate();
            entityManager.getTransaction().commit();

            return gsList;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public <S extends Game_Sales> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Game_Sales> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Game_Sales> findAll() {
        return List.of();
    }

    @Override
    public List<Game_Sales> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Game_Sales entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Game_Sales> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
