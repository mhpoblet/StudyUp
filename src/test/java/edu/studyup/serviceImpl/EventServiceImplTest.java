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
		cal.add(Calendar.YEAR, 1); // to get previous year add -1
		Date nextYear = cal.getTime();
		
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(nextYear);
		event2.setName("Event 1");
		Location location2 = new Location(-122, 37);
		event2.setLocation(location2);
		List<Student> eventStudents2 = new ArrayList<>();
		eventStudents2.add(student);
		event2.setStudents(eventStudents2);
		
		DataStorage.eventData.put(event2.getEventID(), event2);
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
	void testUpdateEvent_NameTooLong_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Too longToo longToo longToo longToo longToo longToo long");
		  });
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testUpdateMethod_event_null_badcases() {
		Event event = null;
			Assertions.assertThrows(StudyUpException.class, () -> { 
				eventServiceImpl.updateEvent(event);
			}); 
	}
	
	@Test
	void testgetActiveEvents_GoodCase() throws StudyUpException {
		assertEquals(eventServiceImpl.getActiveEvents().size(), 1);
	}
	
	@Test
	void testgetActiveEvents_EventInFuture_BadCase() {
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.getActiveEvents();
		  });
	}
	
	@Test
	void testBadCase() {
		assertEquals(DataStorage.eventData.size(),1);
	}
}
