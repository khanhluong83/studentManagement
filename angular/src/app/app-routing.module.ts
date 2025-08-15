import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CourseListComponent } from './components/course-list/course-list.component';
import { CourseFormComponent } from './components/course-form/course-form.component';
import { StudentListComponent } from './components/student-list/student-list.component';
import { StudentFormComponent } from './components/student-form/student-form.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: HomeComponent
  },
  {
      path: 'course-list',
      component: CourseListComponent
  },
  {
      path: 'course-add',
      component: CourseFormComponent
  },
  {
      path: 'course-edit/:id',
      component: CourseFormComponent
  },
  {
      path: 'student-list',
      component: StudentListComponent
  },
  {
      path: 'student-add',
      component: StudentFormComponent
  },
  {
      path: 'student-edit/:id',
      component: StudentFormComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
