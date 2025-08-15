import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Course } from '../models/course.type';
import { SearchResult } from '../models/search-result.type';
import { ApiResponse } from '../models/api-response.type';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  API_URL: string = environment.api_url + 'course';

  constructor(private http: HttpClient) { }

  getAll(pageSize: number, pageIndex: number,
    sortCol: String, sortDir: String
  ) {
    var url = this.API_URL + '?';
    url += '&page=' + pageIndex;
    url += '&size=' + pageSize;
    if (sortCol) {
      if (!sortDir) {
        sortDir = 'asc';
      }
      url += '&sort=' + sortCol + ',' + sortDir;
    }

    return this.http.get<SearchResult<Course>>(url);
  }

  getById(id: number) {
    const url = this.API_URL + '/' + id;
    return this.http.get<Course>(url);
  }

  create(course: Course) {
    const url = this.API_URL;
    return this.http.post<ApiResponse<Course>>(url, course);
  }

  update(id: number, course: Course) {
    const url = this.API_URL + '/' + id;
    return this.http.put<ApiResponse<Course>>(url, course);
  }

  delete(id: number) {
    const url = this.API_URL + '/' + id;
    return this.http.delete<ApiResponse<Course>>(url);
  }
}
