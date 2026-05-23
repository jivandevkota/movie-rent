import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({ providedIn: 'root' })
export class AdminRentalService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllRentals(page: number = 0, size: number = 20, status: string = 'all'): Observable<any> {
    return this.http.get(`${this.url}/admin/rentals?page=${page}&size=${size}&status=${status}`);
  }

  forceReturn(rentalId: number): Observable<void> {
    return this.http.put<void>(`${this.url}/admin/rentals/${rentalId}/return`, {});
  }

  processRental(customerId: number, filmId: number): Observable<any> {
    return this.http.post(`${this.url}/admin/rentals?customerId=${customerId}&filmId=${filmId}`, {});
  }
}
