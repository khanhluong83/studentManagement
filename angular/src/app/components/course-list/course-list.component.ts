import { Component, inject, signal, ViewChild } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { Course } from '../../models/course.type';

@Component({
  selector: 'app-course-list',
  standalone: false,
  templateUrl: './course-list.component.html',
  styleUrl: './course-list.component.css'
})
export class CourseListComponent {

  courseService = inject(CourseService);

  displayedColumns: String[] = ['id', 'code', 'name', 'startDate', 'endDate', 'actions'];
  dataSource = new MatTableDataSource<Course>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  itemCount = signal<number>(0);
  pageSize = signal<number>(5);
  pageIndex = signal<number>(0);
  sortCol = signal<String>('');
  sortDir = signal<String>('');


  constructor(private router: Router) {

  }

  ngOnInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    
    this.loadSearch();
  }

  loadSearch() {
    this.courseService.getAll(this.pageSize(), this.pageIndex(), this.sortCol(), this.sortDir())
    .subscribe((searchResult) => {
      this.itemCount.set(searchResult.count);
      this.dataSource.data = searchResult.items;
    }
    );
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
      this.courseService.delete(id).subscribe(response => {
        alert(response.success);
        this.loadSearch();
      })
    }
  }
}
