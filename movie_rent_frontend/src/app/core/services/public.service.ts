import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable(
    {providedIn: 'root'}
)

export class PublicService{
    private readonly url = environment.apiUrl;
    constructor(private http: HttpClient) {}

  getAllCategory(): Observable<any> {
    return this.http.get<any>(
      `${this.url}/categories`
    );
  }

  getFilms(page: number): Observable<any> {
     return this.http.get<any>(
      `${this.url}/films?page=${page}&size=20`
    );
  }

  getByCategory(
    id: number,
    page: number
  ): Observable<any> {

    return this.http.get<any>(
      `${this.url}/category/${id}/films?page=${page}&size=20`
    );
  }

  getBySearch(
    search: string,
    page: number
  ): Observable<any> {

    return this.http.get<any>(
      `${this.url}/films/search?title=${search}&page=${page}&size=20`
    );
  }

  getFilmDetails(
    filmId: number
  ): Observable<any> {

    return this.http.get<any>(
      `${this.url}/films/${filmId}`
    );
  }
}