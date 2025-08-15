import { Component, inject, signal, ViewChild } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { MatTableDataSource } from '@angular/material/table';
import { Student } from '../../models/student.type';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student-list',
  standalone: false,
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css'
})
export class StudentListComponent {

  studentService = inject(StudentService);

  displayedColumns: String[] = ['id', 'firstName', 'lastName', 'email', 'phone', 'actions'];
  dataSource = new MatTableDataSource<Student>([]);

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
    this.studentService.getAll(this.pageSize(), this.pageIndex(), this.sortCol(), this.sortDir())
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
    this.router.navigate(["/student-edit/", id]);
  }

  delete(id: number) {
    const isDeleted = confirm("Do you want to delete the Student?");
    if (isDeleted) {
      this.studentService.delete(id).subscribe(response => {
        alert(response.success);
        this.loadSearch();
      })
    }
  }

}
