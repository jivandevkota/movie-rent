import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCart(customerId: number): Observable<any> {
    return this.http.get(`${this.url}/customers/${customerId}/cart`);
  }

  addToCart(customerId: number, filmId: number, quantity: number = 1): Observable<any> {
    return this.http.post(`${this.url}/customers/${customerId}/cart/items`, { filmId, quantity });
  }

  updateItem(customerId: number, cartItemId: number, quantity: number): Observable<any> {
    return this.http.put(`${this.url}/customers/${customerId}/cart/items/${cartItemId}`, { quantity });
  }

  removeItem(customerId: number, cartItemId: number): Observable<any> {
    return this.http.delete(`${this.url}/customers/${customerId}/cart/items/${cartItemId}`);
  }

  clearCart(customerId: number): Observable<any> {
    return this.http.delete(`${this.url}/customers/${customerId}/cart`);
  }
}
