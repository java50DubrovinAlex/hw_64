package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.cars.service.model.*;

public interface TradeDealRepo extends JpaRepository<TradeDeal, Long> {
List<TradeDeal> findByCarNumber(String carNumber);

List<TradeDeal> findByCarOwnerId(long id);

@Query(value="select count(cars.:model) as tm_count from cars join trade_deals td"
+"on cars.car_number=td.car_number"
+"where td.date between :startDate and :endDate", nativeQuery = true )
long countTradeDealAtMonthModel (String model, LocalDate startDate, LocalDate endDate);

}
