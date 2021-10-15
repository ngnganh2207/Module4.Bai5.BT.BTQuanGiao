package com.codegym.controller;

import com.codegym.model.Student;
import com.codegym.model.StudentForm;
import com.codegym.service.implStudent.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller

@PropertySource("classpath:upload_file.properties")
public class StudentController {
    @Value("${file-upload}")
    private String fileUpload;

    @Autowired
    private IStudentService studentService;

    @RequestMapping("/")
    public ModelAndView listStudent(){
        Iterable<Student> students= studentService.findAll();
        ModelAndView modelAndView= new ModelAndView("student/liststudent");
        modelAndView.addObject("listStudent", students);
        return modelAndView;
    }

    @GetMapping("/create/student")
    public ModelAndView showCreateStudentForm(){
        ModelAndView modelAndView= new ModelAndView("student/create");
        modelAndView.addObject("student", new StudentForm());
        return modelAndView;
    }

    @PostMapping("/create/student")
    public ModelAndView saveStudent(@ModelAttribute("student") StudentForm studentForm, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message","Create Successfully");
        MultipartFile multipartFile= studentForm.getImage();
        String fileName= multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpload+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Student studentCreate= new Student(studentForm.getId(),studentForm.getName(),studentForm.getAddress(),studentForm.getEmail(),fileName);
        studentService.save(studentCreate);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/findName")
    public ModelAndView findByName(@RequestParam("findName") String findName){
       List<Student> students=  studentService.findByName(findName);
       ModelAndView modelAndView= new ModelAndView("student/studentbyname");
       modelAndView.addObject("studentList",students);
        return modelAndView;
    }

    @GetMapping("/findId")
    public ModelAndView findById(@RequestParam Long findId){
        Student studentId= studentService.findById(findId);
        ModelAndView modelAndView= new ModelAndView("student/studentbyid");
        modelAndView.addObject("student", studentId);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id){
        Student student= studentService.findById(id);
        if(student!=null){
            ModelAndView modelAndView= new ModelAndView("student/edit");
            modelAndView.addObject("studentEdit", student);
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("Error.404");
            return modelAndView;
        }


    }

    @PostMapping("/edit/{id}")
    public ModelAndView editStudent(@PathVariable("id") Long id,@ModelAttribute("student") StudentForm studentForm, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message","Edit Successfully");
        MultipartFile multipartFile= studentForm.getImage();
        String fileName= multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpload+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Student studentEdit= new Student(studentForm.getId(),studentForm.getName(),studentForm.getAddress(),studentForm.getEmail(),fileName);
        Student studentEdit= studentService.findById(id);
        studentEdit.setName(studentForm.getName());
        studentEdit.setEmail(studentForm.getEmail());
        studentEdit.setAddress(studentForm.getAddress());
        studentEdit.setImage(fileName);
        studentService.save(studentEdit);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteStudentForm(@PathVariable("id") Long id){
        Student student= studentService.findById(id);
        if(student!=null){
            ModelAndView modelAndView= new ModelAndView("student/delete");
            modelAndView.addObject("studentDelete", student);
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("Error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete")
    public ModelAndView deleteStudent(@ModelAttribute("studentDelete") Student student){
        studentService.remove(student);
        return new ModelAndView("redirect:/");
    }
}
