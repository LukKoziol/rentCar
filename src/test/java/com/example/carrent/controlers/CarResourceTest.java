package com.example.carrent.controlers;

import antlr.ASTNULLType;
import com.example.carrent.model.Car;
import jdk.nashorn.internal.ir.FunctionNode;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc

class CarResourceTest {
    private static final String UPDATED_MODEL = null;
    private static final String DEFAULT_MODEL = null;
    private static final String DEFAULT_ENGINE = null;
    private static final String DEFAULT_NR_RE = null;
    private static final String DEFAULT_VIN = null;
    private static final String UPDATED_ENGINE = null;
    private static final String UPDATED_NR_RE = null;
    private static final String UPDATED_VIN = null;
    @Autowired
    private MockMvc restCarMockMvc;
    private Object TestUtil;

    @org.junit.jupiter.api.Test
    public void createCar() throws Exception {
        //int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car
        restCarMockMvc.perform(post("/api/cars")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isCreated());

        // Validate the Car in the database
        ASTNULLType carRepository = null;
        List<Car> carList = (List<Car>) carRepository.findAll(null);
        int databaseSizeBeforeCreate = 0;
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCar.getEngine()).isEqualTo(DEFAULT_ENGINE);
        assertThat(testCar.getNrRe()).isEqualTo(DEFAULT_NR_RE);
        assertThat(testCar.getVin()).isEqualTo(DEFAULT_VIN);
    }


    @org.junit.jupiter.api.Test
    public void updateCar() throws Exception {
        // Initialize the database
        JpaRepository<Object, Object> carRepository = null;

        FunctionNode car = null;
        carRepository.saveAndFlush(car);
        

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = (Car) carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
                .model(UPDATED_MODEL)
                .engine(UPDATED_ENGINE)
                .nrRe(UPDATED_NR_RE)
                .vin(UPDATED_VIN);

        restCarMockMvc.perform(put("/api/cars")
                .contentType(TestUtil.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedCar)))
                .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getEngine()).isEqualTo(UPDATED_ENGINE);
        assertThat(testCar.getNrRe()).isEqualTo(UPDATED_NR_RE);
        assertThat(testCar.getVin()).isEqualTo(UPDATED_VIN);
    }
    @org.junit.jupiter.api.Test
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
                .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
                .andExpect(jsonPath("$.[*].engine").value(hasItem(DEFAULT_ENGINE)))
                .andExpect(jsonPath("$.[*].nrRe").value(hasItem(DEFAULT_NR_RE)))
                .andExpect(jsonPath("$.[*].vin").value(hasItem(DEFAULT_VIN)));
    }

    @org.junit.jupiter.api.Test
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(car.getId().intValue()))
                .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
                .andExpect(jsonPath("$.engine").value(DEFAULT_ENGINE))
                .andExpect(jsonPath("$.nrRe").value(DEFAULT_NR_RE))
                .andExpect(jsonPath("$.vin").value(DEFAULT_VIN));
    }

    @Test
    public void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
                .accept(TestUtil.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
}