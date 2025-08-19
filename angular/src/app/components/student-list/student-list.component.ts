import { Component, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { MatTableDataSource } from '@angular/material/table';
import { Student } from '../../models/student.type';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { STUDENT_SEARCH_DETAULT, StudentSearch } from '../../models/student-search.type';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-student-list',
  standalone: false,
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css'
})
export class StudentListComponent implements OnInit, OnDestroy {

  displayedColumns: String[] = ['id', 'firstName', 'lastName', 'email', 'phone', 'courseNameList', 'actions'];
  dataSource = new MatTableDataSource<Student>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  itemCount = signal<number>(0);
  pageSize = signal<number>(5);
  pageIndex = signal<number>(0);
  sortCol = signal<String>('');
  sortDir = signal<String>('');

  searchForm:StudentSearch = {... STUDENT_SEARCH_DETAULT};

  subscriptions: Subscription[] = [];

  constructor(private router: Router, private studentService: StudentService) {

  }

  ngOnInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    
    this.loadSearch();
  }

  loadSearch() {
    console.log(this.searchForm);
    const sub = this.studentService.getAll(this.searchForm, this.pageSize(), this.pageIndex(), this.sortCol(), this.sortDir())
    .subscribe((searchResult) => {
      this.itemCount.set(searchResult.count);
      this.dataSource.data = searchResult.items;
    }
    );
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
    this.router.navigate(["/student-edit/", id]);
  }

  delete(id: number) {
    let isDeleted = confirm("Do you want to delete the Student?");
    if (isDeleted) {
      const sub = this.studentService.delete(id).subscribe({
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
    for (let subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

}
