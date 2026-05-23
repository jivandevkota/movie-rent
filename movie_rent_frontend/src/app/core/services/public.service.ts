import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "src/environments/environment";

@Injectable(
  {providedIn: 'root'}
)

export class PublicService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {
  }


  login(email: string, password: string): Observable<any> {
      return this.http.post<any>(
        `${this.url}/auth/login`, { email, password }
      );
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('customer') !== null;
  }

  logout(): void {
    localStorage.removeItem('customer');
  }

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

  getCustomerRentals(customerId: number): Observable<any> {
    return this.http.get<any>(
      `${this.url}/customers/${customerId}/rentals`
    );
  }

  rentFilm(customerId: number, filmId: number): Observable<any> {
    return this.http.post<any>(`${this.url}/customers/${customerId}/rentals`, { filmId });
  }

  returnFilm(customerId: number, rentalId: number): Observable<any> {
    return this.http.put<any>(`${this.url}/customers/${customerId}/rentals/${rentalId}/return`, {});
  }

  register(firstName: string, lastName: string, email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.url}/auth/register`, { firstName, lastName, email, password });
  }

  getFilmDetails(filmId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/films/${filmId}`);
  }

  getActor(actorId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/actors/${actorId}`);
  }

  getCustomerProfile(customerId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/customers/${customerId}/profile`);
  }

  getPopularFilms(): Observable<any> {
    return this.http.get<any>(`${this.url}/films/popular`);
  }
}
