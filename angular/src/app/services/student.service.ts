import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Student } from '../models/student.type';
import { SearchResult } from '../models/search-result.type';
import { ApiResponse } from '../models/api-response.type';
import { StudentSearch } from '../models/student-search.type';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  API_URL: string = environment.api_url + 'student';

  constructor(private http: HttpClient) { }

  getAll(studentSearch: StudentSearch,
      pageSize: number, pageIndex: number,
      sortCol: String, sortDir: String
  ) {
    var url = this.API_URL + '?';
    if (studentSearch.firstName && studentSearch.firstName.length > 0) {
      url += '&firstName=' + studentSearch.firstName;
    }
    if (studentSearch.lastName && studentSearch.lastName.length > 0) {
      url += '&lastName=' + studentSearch.lastName;
    }
    url += '&page=' + pageIndex;
    url += '&size=' + pageSize;
    if (sortCol) {
      if (!sortDir) {
        sortDir = 'asc';
      }
      url += '&sort=' + sortCol + ',' + sortDir;
    }

    return this.http.get<SearchResult<Student>>(url);
  }

  getById(id: number) {
    const url = this.API_URL + '/' + id;
    return this.http.get<Student>(url);
  }

  create(student: Student) {
    const url = this.API_URL;
    return this.http.post<ApiResponse<Student>>(url, student);
  }

  update(id: number, student: Student) {
    const url = this.API_URL + '/' + id;
    return this.http.put<ApiResponse<Student>>(url, student);
  }

  delete(id: number) {
    const url = this.API_URL + '/' + id;
    return this.http.delete<ApiResponse<Student>>(url);
  }

}
