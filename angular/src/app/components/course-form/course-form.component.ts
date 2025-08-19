import { Component, OnDestroy, OnInit } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { BLANK_COURSE, Course } from '../../models/course.type';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-course-form',
  standalone: false,
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.css'
})
export class CourseFormComponent implements OnInit, OnDestroy {

  courseId: number = 0;
  courseItem: Course = {... BLANK_COURSE};
  subscriptions: Subscription[] = [];

  constructor(private route: ActivatedRoute, private router: Router,
    private courseService: CourseService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: any) => {
      this.courseId = params.id;
    });

    if (this.courseId > 0) {
      this.loadCourse(this.courseId);
    }
  }

  loadCourse(id: number) {
    const sub = this.courseService.getById(id).subscribe({
      next: response => {
              this.courseItem = response;
            },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          alert(error.error);
          this.router.navigateByUrl("/course-list");
        }
      }
    });
    this.subscriptions.push(sub);
  }

  createCourse(courseForm: Course) {
    const sub = this.courseService.create(courseForm).subscribe({
      next:  response => {
        alert(response.success);
        this.router.navigate(["/course-list"]);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 400) {
          alert(error.error);
        }
      }
    });
    this.subscriptions.push(sub);
  }

  editCourse(id: number, courseForm: Course) {
    const sub = this.courseService.update(id, courseForm).subscribe({
      next: response => {
              alert(response.success);
              this.router.navigate(["/course-list"]);
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
    for (let subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }
}
