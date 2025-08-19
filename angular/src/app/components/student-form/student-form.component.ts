import { Component, OnDestroy, OnInit } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { BLANK_STUDENT, Student } from '../../models/student.type';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { CourseService } from '../../services/course.service';
import { Course } from '../../models/course.type';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-student-form',
  standalone: false,
  templateUrl: './student-form.component.html',
  styleUrl: './student-form.component.css'
})
export class StudentFormComponent implements OnInit, OnDestroy {

  studentId: number = 0;
  studentItem: Student = {... BLANK_STUDENT};
  courses: Course[] = [];

  subscriptions: Subscription[] = [];

  constructor(private route: ActivatedRoute, private router: Router,
    private studentService: StudentService,
    private courseService: CourseService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: any) => {
      this.studentId = params.id;
    });

    if (this.studentId > 0) {
      this.loadStudent(this.studentId);
    }
    this.loadCourses();
  }

  loadStudent(id: number) {
    const sub = this.studentService.getById(id).subscribe({
      next: response => {
            this.studentItem = response;
          },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          alert(error.error);
          this.router.navigateByUrl("/student-list");
        }
      } 
    });
    this.subscriptions.push(sub);
  }

  loadCourses() {
    const sub = this.courseService.getAll(-1, 0, 'code', 'asc').subscribe(
      response => {
        this.courses = response.items;
      }
    );
    this.subscriptions.push(sub);
  }

  createStudent(studentForm: Student) {
    const sub = this.studentService.create(studentForm).subscribe({
      next:  response => {
        alert(response.success);
        this.router.navigate(["/student-list"]);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert(error.error);
        }
      }
    });
    this.subscriptions.push(sub);
  }

  editStudent(id: number, studentForm: Student) {
    const sub = this.studentService.update(id, studentForm).subscribe({
      next: response => {
        alert(response.success);
        this.router.navigate(["/student-list"]);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 400 || error.status === 404) {
          alert(error.error);
        }
      }
    });
    this.subscriptions.push(sub);
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

}
