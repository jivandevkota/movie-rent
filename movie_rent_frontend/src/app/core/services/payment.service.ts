import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getPayments(customerId: number): Observable<any> {
    return this.http.get(`${this.url}/customers/${customerId}/payments`);
  }
}