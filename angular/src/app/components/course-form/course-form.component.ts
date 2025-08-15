import { Component, inject } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { Course } from '../../models/course.type';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-course-form',
  standalone: false,
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.css'
})
export class CourseFormComponent {

  courseService = inject(CourseService);
  courseId: number = 0;
  courseItem: Course = {id: 0, code: "", name: "", startDate: "", endDate: ""};

  constructor(private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe((params: any) => {
      this.courseId = params.id;
    });
  }

  ngOnInit(): void {
    if (this.courseId > 0) {
      this.loadCourse(this.courseId);
    }
  }

  loadCourse(id: number) {
    this.courseService.getById(id).subscribe(response => {
      this.courseItem = response;
    });
  }

  createCourse(courseForm: Course) {
    this.courseService.create(courseForm).subscribe({
      next:  response => {
        alert(response.success);
        this.router.navigate(["/course-list"]);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert(error.error);
        }
      }
    })
  }

  editCourse(id: number, courseForm: Course) {
    this.courseService.update(id, courseForm).subscribe(response => {
      alert(response.success);
      this.router.navigate(["/course-list"]);
    })
  }
}
