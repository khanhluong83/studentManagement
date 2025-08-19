import { Component, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { Course } from '../../models/course.type';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-course-list',
  standalone: false,
  templateUrl: './course-list.component.html',
  styleUrl: './course-list.component.css'
})
export class CourseListComponent implements OnInit, OnDestroy {

  displayedColumns: String[] = ['id', 'code', 'name', 'startDate', 'endDate', 'actions'];
  dataSource = new MatTableDataSource<Course>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  itemCount = signal<number>(0);
  pageSize = signal<number>(5);
  pageIndex = signal<number>(0);
  sortCol = signal<String>('');
  sortDir = signal<String>('');

  subscriptions: Subscription[] = [];

  constructor(private router: Router, private courseService: CourseService) {

  }

  ngOnInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    
    this.loadSearch();
  }

  loadSearch() {
    const sub = this.courseService.getAll(this.pageSize(), this.pageIndex(), this.sortCol(), this.sortDir())
    .subscribe((searchResult) => {
      this.itemCount.set(searchResult.count);
      this.dataSource.data = searchResult.items;
    });
    this.subscriptions.push(sub);
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadSearch();
  }

  onSortChange(event: Sort) {
    if (event.direction) {
      this.sortCol.set(event.active);
      this.sortDir.set(event.direction);
    } else {
      this.sortCol.set('');
      this.sortDir.set('');
    }
    this.loadSearch();
  }

  edit(id: number) {
    this.router.navigate(["/course-edit/", id]);
  }

  delete(id: number) {
    const isDeleted = confirm("Do you want to delete the Course?");
    if (isDeleted) {
      const sub = this.courseService.delete(id).subscribe({
        next: response => {
              alert(response.success);
              this.loadSearch();
            },
        error: (error: HttpErrorResponse) => {
          if (error.status === 404) {
            alert(error.error);
          }
        }
      });
      this.subscriptions.push(sub);
    }
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }
}
