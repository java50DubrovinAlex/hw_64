package telran.cars.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.*;
import telran.cars.exceptions.*;
import telran.cars.repo.*;
import telran.cars.service.model.*;
@Service
@RequiredArgsConstructor
@Slf4j
public class CarsServiceImpl implements CarsService {
	final CarRepo carRepo;
	final CarOwnerRepo carOwnerRepo;
	final ModelRepo modelRepo;
	final TradeDealRepo tradeDealRepo;
	@Override
	@Transactional
	public PersonDto addPerson(PersonDto personDto) {
		if(carOwnerRepo.existsById(personDto.id())) {
			throw new IllegalPersonsStateException();
		}
		CarOwner carOwner = CarOwner.of(personDto);
		carOwnerRepo.save(carOwner);
		log.debug("person {} has been saved", personDto);
		return personDto;
	}

	@Override
	@Transactional
	public CarDto addCar(CarDto carDto) {
		if(carRepo.existsById(carDto.number())) {
			throw new IllegalCarsStateException();
		}
		Model model = modelRepo.findById(new ModelYear(carDto.model(), carDto.year()))
				.orElseThrow(() -> new ModelNotFoundException());
		Car car = Car.of(carDto);
		car.setModel(model);
		carRepo.save(car);
		log.debug("car {} has been saved", carDto);
		return carDto;
	}

	@Override
	@Transactional
	public PersonDto updatePerson(PersonDto personDto) {
		CarOwner carOwner = carOwnerRepo.findById(personDto.id())
				.orElseThrow(() -> new PersonNotFoundException());
		carOwner.setEmail(personDto.email());
		return personDto;
	}
	// TODO 
	//HW #63
	//find Car having being deleted owner
	//If such car exists, set null as the car owner
	//after that delete by the method deleteById from carOwnerRepo
	@Override
	@Transactional
	public PersonDto deletePerson(long id) {
//		List <Car> carsOwnedByCarOwner;
		CarOwner  deletedCarOwner = carOwnerRepo.findById(id).orElseThrow(() -> new PersonNotFoundException());
		
		carOwnerRepo.deleteById(id);
		log.debug("person {} has been delet", deletedCarOwner.build());
		return deletedCarOwner.build();
	}

	@Override
	@Transactional
	public CarDto deleteCar(String carNumber) {
		Car deletedCar = carRepo.findById(carNumber).orElseThrow(() -> new CarNotFoundException());
		
		CarDto  deletedCarDto = deletedCar.build();
		carRepo.deleteById(carNumber);
		log.debug("car {} has been delet", deletedCarDto);
		return deletedCarDto;
	}

	@Override
	@Transactional
	public TradeDealDto purchase(TradeDealDto tradeDealDto) {
		Car car = carRepo.findById(tradeDealDto.carNumber())
				.orElseThrow(() -> new CarNotFoundException());
		CarOwner carOwner = null;
		Long personId = tradeDealDto.personId();
		if ( personId != null) {
			carOwner = carOwnerRepo.findById(personId)
					.orElseThrow(() -> new PersonNotFoundException());
			if(car.getCarOwner().getId() == personId) {
				throw new TradeDealIllegalStateException();
			}
		}
		TradeDeal tradeDeal = new TradeDeal();
		tradeDeal.setCar(car);
		tradeDeal.setCarOwner(carOwner);
		tradeDeal.setDate(LocalDate.parse(tradeDealDto.date()));
		car.setCarOwner(carOwner);
		tradeDealRepo.save(tradeDeal);
		log.debug("trade: {} has been saved", tradeDealDto);
		return tradeDealDto;
	}

	@Override
	public List<CarDto> getOwnerCars(long id) {
		// Not Implemented yet
		return null;
	}

	@Override
	public PersonDto getCarOwner(String carNumber) {
		// Not Implemented yet
		return null;
	}

	@Override
	public List<String> mostPopularModels() {
		// Not Implemented yet
		
		return null;
	}

	@Override
	@Transactional
	public ModelDto addModel(ModelDto modelDto) {
		// TODO Auto-generated method stub
		// HW #63 Write the method similar to the method addPerson
		ModelYear modelYear = new ModelYear(modelDto.getModelName(), modelDto.getModelYear());
		if(modelRepo.existsById(modelYear)) {
			throw new IllegalModelStateException();
		}
		Model model = Model.of(modelDto);
		modelRepo.save(model);
		log.debug("model {} has been saved", modelDto);
		return modelDto;
	}
	

	@Override
	public long countTradeDealAtMonthModel(String model, LocalDate startDate, LocalDate endDate) {
		long res = tradeDealRepo.countTradeDealAtMonthModel(model, startDate, endDate);
		log.debug("model{} has tradeDeal{} between {} and {}", model, res, startDate, endDate);
		return res;
	}

	@Override
	public String findMostPopularModelNameByOwnerAges(LocalDate minAge, LocalDate maxAge) {
		String res = carRepo.findMostPopularModelNameByOwnerAges(minAge, maxAge);
		log.debug("model{} between age {} and {}", res, minAge, maxAge);
		return res;
	}

	@Override
	public String findOneMostPopularColorModel(String model) {
		String res = modelRepo.findOneMostPopularColorModel(model);
		log.debug("most popular model color {}", res);
		return res;
	}

	@Override
	public EnginePowerCapacity findMinEnginePowerCapacityByOwnerAges(LocalDate minAge, LocalDate maxAge) {
		EnginePowerCapacity res = modelRepo.findMinEnginePowerCapacityByOwnerAges(minAge, maxAge);
		log.debug("owner heving age between {} and {} min engine power: {}, min capacity {}", minAge, maxAge, res.getPower(), res.getCapacity());
		return res;
	}

}
