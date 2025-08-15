import { Component, inject } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student.type';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { CourseService } from '../../services/course.service';
import { Course } from '../../models/course.type';

@Component({
  selector: 'app-student-form',
  standalone: false,
  templateUrl: './student-form.component.html',
  styleUrl: './student-form.component.css'
})
export class StudentFormComponent {

  studentService = inject(StudentService);
  courseService = inject(CourseService);

  studentId: number = 0;
  studentItem: Student = {id: 0, firstName: "", lastName: "", email: "", phone: "", courseIdList: []};
  courses: Course[] = [];

  constructor(private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe((params: any) => {
      this.studentId = params.id;
    });
  }

  ngOnInit(): void {
    if (this.studentId > 0) {
      this.loadStudent(this.studentId);
    }
    this.loadCourses();
  }

  loadStudent(id: number) {
    this.studentService.getById(id).subscribe(response => {
      this.studentItem = response;
    });
  }

  loadCourses() {
    this.courseService.getAll(-1, 0, 'code', 'asc').subscribe(
      response => {
        this.courses = response.items;
      }
    )
  }

  createStudent(studentForm: Student) {
    this.studentService.create(studentForm).subscribe({
      next:  response => {
        alert(response.success);
        this.router.navigate(["/student-list"]);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert(error.error);
        }
      }
    })
  }

  editStudent(id: number, studentForm: Student) {
    this.studentService.update(id, studentForm).subscribe(response => {
      alert(response.success);
      this.router.navigate(["/student-list"]);
    })
  }

}
