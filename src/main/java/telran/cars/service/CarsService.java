package telran.cars.service;

import java.time.LocalDate;
import java.util.List;

import telran.cars.dto.*;

public interface CarsService {
PersonDto addPerson(PersonDto personDto);
CarDto addCar(CarDto carDto);
ModelDto addModel(ModelDto modelDto);
PersonDto updatePerson(PersonDto personDto);
PersonDto deletePerson(long id);
CarDto deleteCar(String carNumber);
TradeDealDto purchase(TradeDealDto tradeDeal);
List<CarDto> getOwnerCars(long id);
PersonDto getCarOwner(String carNumber);
List<String> mostPopularModels();
long  countTradeDealAtMonthModel (String model, LocalDate startDate, LocalDate endDate);
String findMostPopularModelNameByOwnerAges(LocalDate mintAge, LocalDate maxAge);
String findOneMostPopularColorModel(String model);
EnginePowerCapacity findMinEnginePowerCapacityByOwnerAges(LocalDate minAge, LocalDate maxAge);
}
