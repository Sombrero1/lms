package com.example.demo.service.mappers;
import org.modelmapper.ModelMapper;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MapperLessonDtoService {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Autowired
    private ModelMapper modelMapper;

    public Lesson convertToEntityLesson(LessonDto lessonDTO) {
        Lesson lesson = modelMapper.map(lessonDTO, Lesson.class);
        return lesson;
    }
    public LessonDto convertToDTOLesson(Lesson lesson) {
        LessonDto lessonDto = modelMapper.map(lesson, LessonDto.class);
        lessonDto.setCourseId(lesson.getCourse().getId());
        return lessonDto;
    }

}
