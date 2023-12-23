
package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * TODO
 * Implement all the unit test in its corresponding class.
 * Make sure to be as exhaustive as possible. Coverage is checked ;)
 */

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Doctor getDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setFirstName("Mariano");
        doctor.setLastName("Pelufo");
        doctor.setAge(24);
        doctor.setEmail("m.pelufo@accwe.com");
        return doctor;
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        Doctor doctor = getDoctor();

        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(doctor.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(doctor.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(doctor.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(doctor.getEmail())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetDoctorById() throws Exception {
        long id = 5;
        Doctor doctor = getDoctor();
        doctor.setId(id);

        assertThat(doctor.getId()).isEqualTo(id);

        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(doctor.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(doctor.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(doctor.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(doctor.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyDoctorById() throws Exception {
        long id = 23;

        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetTwoDoctors() throws Exception {
        Doctor doctor1 = getDoctor();

        Doctor doctor2 = getDoctor();
        doctor2.setId(2);
        doctor2.setFirstName("Susana");
        doctor2.setLastName("Gimenez");
        doctor2.setAge(42);
        doctor2.setEmail("lasu@accwe.com");

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor1);
        doctors.add(doctor2);

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(doctors.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", CoreMatchers.is(doctors.get(0).getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", CoreMatchers.is(doctors.get(0).getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", CoreMatchers.is(doctors.get(1).getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age", CoreMatchers.is(doctors.get(1).getAge())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyDoctors() throws Exception {
        when(doctorRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteDoctorById() throws Exception {
        long id = 3;
        Doctor doctor = getDoctor();
        doctor.setId(id);

        assertThat(doctor.getId()).isEqualTo(id);

        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).deleteById(any(Long.class));

        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteDoctorById() throws Exception {
        long id = 23;

        when(doctorRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllDoctors() throws Exception {
        doNothing().when(doctorRepository).deleteAll();

        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }
}

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Patient getPatient() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("Ricardo");
        patient.setLastName("Fort");
        patient.setAge(43);
        patient.setEmail("ricky.fort@accwe.com");
        return patient;
    }

    @Test
    void shouldCreatePatient() throws Exception {
        Patient patient = getPatient();

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(patient.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(patient.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(patient.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(patient.getEmail())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetPatientById() throws Exception {
        long id = 5;
        Patient patient = getPatient();
        patient.setId(id);

        assertThat(patient.getId()).isEqualTo(id);

        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/" + id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(patient.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(patient.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", CoreMatchers.is(patient.getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(patient.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyPatientById() throws Exception {
        long id = 23;

        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetTwoPatients() throws Exception {
        Patient patient1 = getPatient();

        Patient patient2 = getPatient();
        patient2.setId(2);
        patient2.setFirstName("Moria");
        patient2.setLastName("Casan");
        patient2.setAge(42);
        patient2.setEmail("laone@accwe.com");

        List<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(patients.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", CoreMatchers.is(patients.get(0).getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", CoreMatchers.is(patients.get(0).getAge())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", CoreMatchers.is(patients.get(1).getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age", CoreMatchers.is(patients.get(1).getAge())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyPatients() throws Exception {
        when(patientRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeletePatientById() throws Exception {
        long id = 3;
        Patient patient = getPatient();
        patient.setId(id);

        assertThat(patient.getId()).isEqualTo(id);

        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).deleteById(any(Long.class));

        mockMvc.perform(delete("/api/patients/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeletePatientById() throws Exception {
        long id = 23;

        when(patientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/patients/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllPatients() throws Exception {
        doNothing().when(patientRepository).deleteAll();

        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Room getRoom() {
        return new Room("Cardiology");
    }

    @Test
    void shouldCreateRoom() throws Exception {
        Room room = getRoom();

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomName", CoreMatchers.is(room.getRoomName())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetRoomByName() throws Exception {
        Room room = getRoom();

        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/" + room.getRoomName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomName", CoreMatchers.is(room.getRoomName())))
                .andExpect(status().isOk());
    }


    @Test
    void shouldNotGetAnyRoomByName() throws Exception {
        String roomName = "Oncology";

        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/" + roomName))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetTwoRooms() throws Exception {
        Room room1 = getRoom();
        Room room2 = new Room("Oncology");

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(rooms.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roomName", CoreMatchers.is(rooms.get(0).getRoomName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].roomName", CoreMatchers.is(rooms.get(1).getRoomName())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyRooms() throws Exception {
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteRoomByName() throws Exception {
        Room room = getRoom();

        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).deleteById(any(Long.class));

        mockMvc.perform(delete("/api/rooms/" + room.getRoomName()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteRoomByName() throws Exception {
        String roomName = "Oncology";

        when(roomRepository.findByRoomName(any(String.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/rooms/" + roomName))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllRooms() throws Exception {
        doNothing().when(roomRepository).deleteAll();

        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }


}


