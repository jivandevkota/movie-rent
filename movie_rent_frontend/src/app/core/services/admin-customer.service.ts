import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({ providedIn: 'root' })
export class AdminCustomerService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCustomers(page: number = 0, size: number = 20): Observable<any> {
    return this.http.get(`${this.url}/admin/customers?page=${page}&size=${size}`);
  }

  getCustomer(id: number): Observable<any> {
    return this.http.get(`${this.url}/admin/customers/${id}`);
  }

  getCustomerRentals(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/customers/${id}/rentals`);
  }
}
