package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;


    /**
     * TODO
     * Implement tests for each Entity class: Doctor, Patient, Room and Appointment.
     * Make sure you are as exhaustive as possible. Coverage is checked ;)
     */


    @BeforeEach
    public void setUp() {
        p1 = new Patient("Ricardo", "Fort", 43, "ricky.fort@accwe.com");
        d1 = new Doctor("Mariano", "Pelufo", 24, "m.pelufo@accwe.com");
        r1 = new Room("Cardiology");
    }

    @Nested
    class PatientEntityTest {

        @Test
        void shouldCreatePatientWithNoArguments() {
            p1 = new Patient();
            entityManager.persistAndFlush(p1);

            assertThat(p1).isNotNull();
            assertThat(p1.getId()).isPositive();
        }

        @Test
        void shouldCreatePatientWithArguments() {
            entityManager.persistAndFlush(p1);

            Patient retrievedPatient = entityManager.find(Patient.class, p1.getId());

            assertThat(p1.getId()).isPositive();
            assertThat(retrievedPatient.getFirstName()).isEqualTo("Ricardo");
            assertThat(retrievedPatient.getLastName()).isEqualTo("Fort");
            assertThat(retrievedPatient.getAge()).isEqualTo(43);
            assertThat(retrievedPatient.getEmail()).isEqualTo("ricky.fort@accwe.com");

        }

        @Test
        void shouldSetPatientValues() {
            p1 = new Patient();
            p1.setFirstName("Callejero");
            p1.setLastName("Fino");
            p1.setAge(27);
            p1.setEmail("elcalle@accwe.com");

            entityManager.persistAndFlush(p1);
            p1.setId(1);

            assertThat(p1.getId()).isEqualTo(1);
            assertThat(p1.getFirstName()).isEqualTo("Callejero");
            assertThat(p1.getLastName()).isEqualTo("Fino");
            assertThat(p1.getAge()).isEqualTo(27);
            assertThat(p1.getEmail()).isEqualTo("elcalle@accwe.com");
        }
    }

    @Nested
    class DoctorEntityTest {

        @Test
        void shouldCreateDoctorWithNoArguments() {
            d1 = new Doctor();
            entityManager.persistAndFlush(d1);

            assertThat(d1).isNotNull();
            assertThat(d1.getId()).isPositive();
        }

        @Test
        void shouldCreateDoctorWithArguments() {
            d1 = new Doctor("Mariano", "Pelufo", 24, "m.pelufo@accwe.com");
            entityManager.persistAndFlush(d1);

            Doctor retrievedDoctor = entityManager.find(Doctor.class, d1.getId());

            assertThat(d1.getId()).isPositive();
            assertThat(retrievedDoctor.getFirstName()).isEqualTo("Mariano");
            assertThat(retrievedDoctor.getLastName()).isEqualTo("Pelufo");
            assertThat(retrievedDoctor.getAge()).isEqualTo(24);
            assertThat(retrievedDoctor.getEmail()).isEqualTo("m.pelufo@accwe.com");
        }

        @Test
        void shouldSetDoctorValues() {
            d1 = new Doctor();
            d1.setFirstName("Charly");
            d1.setLastName("Garcia");
            d1.setAge(70);
            d1.setEmail("saynomore@accwe.com");

            entityManager.persistAndFlush(d1);
            d1.setId(1);

            assertThat(d1.getId()).isEqualTo(1);
            assertThat(d1.getFirstName()).isEqualTo("Charly");
            assertThat(d1.getLastName()).isEqualTo("Garcia");
            assertThat(d1.getAge()).isEqualTo(70);
            assertThat(d1.getEmail()).isEqualTo("saynomore@accwe.com");
        }
    }

    @Nested
    class RoomEntityTest {

        @Test
        void shouldCreateRoomWithNoArguments() {
            r1 = new Room();

            assertThat(r1.getRoomName()).isNull();
        }

        @Test
        void shouldCreateRoomWithArguments() {
            r1 = new Room("Cardiology");
            entityManager.persistAndFlush(r1);
            Room retrievedRoom = entityManager.find(Room.class, r1.getRoomName());

            assertThat(retrievedRoom)
                    .hasFieldOrPropertyWithValue("roomName", "Cardiology");
        }

    }

    @Nested
    class AppointmentEntityTest {

        private DateTimeFormatter formatter;

        @BeforeEach
        public void setUp() {
            formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            LocalDateTime startsAt1 = LocalDateTime.parse("19:30 24/04/2023", formatter);
            LocalDateTime finishesAt1 = LocalDateTime.parse("20:30 24/04/2023", formatter);
            a1 = new Appointment(p1, d1, r1, startsAt1, finishesAt1);
            a2 = new Appointment(p1, d1, r1, startsAt1, finishesAt1);
            a3 = new Appointment(p1, d1, r1, startsAt1, finishesAt1);
        }

        @Test
        void shouldCreateAppointmentWithNoArguments() {
            a1 = new Appointment();
            entityManager.persistAndFlush(a1);

            assertThat(a1)
                    .hasAllNullFieldsOrPropertiesExcept("id");
        }

        @Test
        void shouldCreateAppointmentWithArguments() {
            entityManager.persistAndFlush(a1);
            Appointment retrievedAppointment = entityManager.find(Appointment.class, a1.getId());

            assertThat(a1.getId()).isPositive();
            assertThat(retrievedAppointment)
                    .hasFieldOrPropertyWithValue("doctor", d1)
                    .hasFieldOrPropertyWithValue("patient", p1)
                    .hasFieldOrPropertyWithValue("room", r1)
                    .hasFieldOrPropertyWithValue("startsAt", a1.getStartsAt())
                    .hasFieldOrPropertyWithValue("finishesAt", a1.getFinishesAt());
        }

        @Test
        void shouldSetAppointmentValues() {
            LocalDateTime startsAt1 = LocalDateTime.parse("19:30 24/04/2023", formatter);
            LocalDateTime finishesAt1 = LocalDateTime.parse("20:30 24/04/2023", formatter);

            a1 = new Appointment();
            a1.setId(3);
            a1.setDoctor(d1);
            a1.setPatient(p1);
            a1.setRoom(r1);
            a1.setStartsAt(startsAt1);
            a1.setFinishesAt(finishesAt1);

            assertThat(a1.getId()).isEqualTo(3);
            assertThat(a1.getDoctor()).isEqualTo(d1);
            assertThat(a1.getPatient()).isEqualTo(p1);
            assertThat(a1.getRoom()).isEqualTo(r1);
            assertThat(a1.getStartsAt()).isEqualTo(startsAt1);
            assertThat(a1.getFinishesAt()).isEqualTo(finishesAt1);
        }

        @Test
        void overlapCasesTest() {
            LocalDateTime someTime = LocalDate.now().atStartOfDay();
            // Case 1: A.starts == B.starts
            a1.setStartsAt(someTime);
            a2.setStartsAt(someTime);
            assertThat(a1.overlaps(a2)).isTrue();

            // Case 2: A.finishes == B.finishes
            a2.setFinishesAt(someTime);
            a3.setFinishesAt(someTime);
            assertThat(a2.overlaps(a3)).isTrue();

            // Case 3: A.starts < B.finishes && B.finishes < A.finishes
            a1.setStartsAt(someTime);
            a2.setFinishesAt(someTime.plusMinutes(30));
            a3.setStartsAt(someTime.plusMinutes(15));
            assertThat(a1.overlaps(a2)).isTrue();
            assertThat(a2.overlaps(a3)).isTrue();

            // Case 4: B.starts < A.starts && A.finishes < B.finishes
            a1.setStartsAt(someTime);
            a1.setFinishesAt(someTime.plusMinutes(30));
            a2.setStartsAt(someTime.minusMinutes(15));
            a3.setFinishesAt(someTime.plusMinutes(15));
            assertThat(a1.overlaps(a2)).isTrue();
            assertThat(a1.overlaps(a3)).isTrue();

            // Case 5: A Does Not overlap with B
            a1.setStartsAt(someTime);
            a1.setFinishesAt(someTime.plusHours(1));
            a2.setStartsAt(someTime.plusHours(2));
            a2.setFinishesAt(someTime.plusHours(3));
            assertThat(a1.overlaps(a2)).isFalse();
        }
    }

}


