package com.example.carrent.controlers;


import com.example.carrent.model.Car;
import com.example.carrent.repository.CarRepository;

import com.example.carrent.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**

        */
@RestController
@RequestMapping("/api")
@Transactional
public class CarResource {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarRepository carRepository;

    public CarResource(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * {@code POST  /cars} : Create a new car.
     *
     * @param car the car to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new car, or with status {@code 400 (Bad Request)} if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to save Car : {}", car);
        if (car.getId() != null) {
            try {
                throw new Exception( "idexists");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Car result = carRepository.save(car);
        return ResponseEntity.created(new URI("/api/cars/" + result.getId()))
     //       .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /cars} : Updates an existing car.
     *
     * @param car the car to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated car,
     * or with status {@code 400 (Bad Request)} if the car is not valid,
     * or with status {@code 500 (Internal Server Error)} if the car couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cars")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to update Car : {}", car);
        if (car.getId() == null) {
            try {
                throw new Exception("Invalid id");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Car result = carRepository.save(car);
        return ResponseEntity.ok()
         //   .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, car.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping("/cars")
    public List<Car> getAllCars(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Cars");
        return carRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /cars/:id} : get the "id" car.
     *
     * @param id the id of the car to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the car, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        log.debug("REST request to get Car : {}", id);
        Optional<Car> car = carRepository.findOneWithEagerRelationships(id);


        return ResponseUtil.wrapOrNotFound(car);
    }

    /**
     * {@code DELETE  /cars/:id} : delete the "id" car.
     *
     * @param id the id of the car to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        log.debug("REST request to delete Car : {}", id);
        carRepository.deleteById(id);
        return ResponseEntity.noContent()
//                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
