package coding.test.repository;

import java.util.List;
import coding.test.model.Game_Sales;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface Game_SalesRepository extends PagingAndSortingRepository<Game_Sales, Integer>, CrudRepository<Game_Sales, Integer> {

    List<Game_Sales> findAll();

    @Query("select count(*), sum(sale_price) from Game_Sales where date_of_sale >= :fromDTS and date_of_sale <= :toDTS")
    public String findGameSold(long fromDTS, long toDTS);

    @Query("select count(*), sum(sale_price) from Game_Sales where game_no = :gameNo and date_of_sale >= :fromDTS and date_of_sale <= :toDTS")
    public String findGameSoldWithGameCode(String gameNo, long fromDTS, long toDTS);

    @Query("select g from Game_Sales g where g.sale_price > :salePrice and g.date_of_sale >= :fromDTS and g.date_of_sale <= :toDTS")
    Page<Game_Sales> findAllByGreaterSalePrice(double salePrice, long fromDTS, long toDTS, Pageable pageable);

    @Query("select g from Game_Sales g where g.sale_price < :salePrice and g.date_of_sale >= :fromDTS and g.date_of_sale <= :toDTS")
    Page<Game_Sales> findAllByLessSalePrice(double salePrice, long fromDTS, long toDTS, Pageable pageable);

}
