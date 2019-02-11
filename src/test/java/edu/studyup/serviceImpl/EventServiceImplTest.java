package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
		
		//Create Event2
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1); 
		Date nextYear = cal.getTime();
		
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(nextYear);
		event2.setName("Event 2");
		Location location2 = new Location(-122, 37);
		event2.setLocation(location2);
		List<Student> eventStudents2 = new ArrayList<>();
		eventStudents2.add(student);
		event2.setStudents(eventStudents2);
		
		DataStorage.eventData.put(event2.getEventID(), event2);
		
		//Create Event3
		Calendar calPast = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1); 
		Date prevYear = cal.getTime();
		
		Event event3 = new Event();
		event3.setEventID(3);
		event3.setDate(prevYear);
		event3.setName("Event 3");
		Location location3 = new Location(-122, 37);
		event3.setLocation(location3);	
		DataStorage.eventData.put(event3.getEventID(), event3);
	
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_AtMost_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "toolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolongtoolong");
		  });
	}
	
	@Test
	void testUpdateEventName_MaxCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "12345678912345678912");
		assertEquals("12345678912345678912", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 40;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 40");
		  });
	}
	
	@Test
	void testgetActiveEvents_GoodCase() throws StudyUpException {
		assertEquals(eventServiceImpl.getActiveEvents().size(), 2);
	}
	
	@Test
	void testgetActiveEvents_Contains() throws StudyUpException {
		int eventID = 2;
		assertTrue(eventServiceImpl.getActiveEvents().contains(DataStorage.eventData.get(eventID)));
	}
	
	@Test
	void testgetPastEvents_GoodCase() throws StudyUpException {
		assertEquals(eventServiceImpl.getPastEvents().size(), 2);	
	}
	
	@Test
	void testgetPastEvents_Contains() throws StudyUpException {
		int eventID = 3;
		assertTrue(eventServiceImpl.getPastEvents().contains(DataStorage.eventData.get(eventID)));
	}
	
	@Test
	void testaddStudentToEvent_GoodCase() throws StudyUpException {
		int eventId = 1;
		Student testStudent = new Student();
		eventServiceImpl.addStudentToEvent(testStudent, eventId);
		assertEquals(DataStorage.eventData.get(eventId).getStudents().size(), 2); 
	}
	
	@Test
	void testaddStudentToEvent_BadCase() throws StudyUpException {
		int eventId = 1;
		Student testStudent = new Student();
		Student testStudent2 = new Student();
		eventServiceImpl.addStudentToEvent(testStudent, eventId);
		eventServiceImpl.addStudentToEvent(testStudent2, eventId);
		assertEquals(DataStorage.eventData.get(eventId).getStudents().size(), 2); 
	}
	
	@Test
	void testaddStudentToEvent_NullEvent() {
		int eventId = 100;
		Student testStudent = new Student();
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(testStudent, eventId);
		});	
	}
	
	@Test
	void testaddStudentToEvent_FirstStudent() throws StudyUpException {
		int eventId = 3;
		Student testStudent = new Student();
		assertNotNull(eventServiceImpl.addStudentToEvent(testStudent, eventId));
	}
	
	@Test
	void testDeleteEvents_GoodCase() throws StudyUpException {
		int eventId = 1;
		eventServiceImpl.deleteEvent(eventId);
		assertFalse(DataStorage.eventData.containsKey(eventId));
	}
}
