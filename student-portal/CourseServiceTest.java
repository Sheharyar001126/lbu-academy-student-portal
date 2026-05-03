package com.lbu.academy;

import com.lbu.academy.entity.Course;
import com.lbu.academy.repository.CourseRepository;
import com.lbu.academy.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void getAllCourses_ReturnsAllCourses() {
        Course c1 = new Course();
        c1.setId(1L);
        c1.setTitle("Software Engineering");
        c1.setDescription("Fundamentals of software design");
        c1.setCredits(30);
        c1.setFee(1500.00);

        Course c2 = new Course();
        c2.setId(2L);
        c2.setTitle("Cloud Computing");
        c2.setDescription("Cloud platforms and services");
        c2.setCredits(20);
        c2.setFee(1200.00);

        when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Software Engineering", result.get(0).getTitle());
        assertEquals("Cloud Computing", result.get(1).getTitle());
    }

    @Test
    void getAllCourses_EmptyList_ReturnsEmpty() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}