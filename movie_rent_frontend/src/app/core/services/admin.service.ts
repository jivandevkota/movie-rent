import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<any> {
    return this.http.get(`${this.url}/admin/dashboard/stats`);
  }

  getFilms(page: number = 0, size: number = 20): Observable<any> {
    return this.http.get(`${this.url}/admin/films?page=${page}&size=${size}`);
  }

  getFilm(filmId: number): Observable<any> {
    return this.http.get(`${this.url}/admin/films/${filmId}`);
  }

  createFilm(data: any): Observable<any> {
    return this.http.post(`${this.url}/admin/films`, data);
  }

  updateFilm(filmId: number, data: any): Observable<any> {
    return this.http.put(`${this.url}/admin/films/${filmId}`, data);
  }

  deleteFilm(filmId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/admin/films/${filmId}`);
  }

  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/categories`);
  }

  getInventory(filmId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/films/${filmId}/inventory`);
  }

  addInventory(filmId: number, storeId: number = 1): Observable<any> {
    return this.http.post(`${this.url}/admin/films/${filmId}/inventory?storeId=${storeId}`, {});
  }

  removeInventory(inventoryId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/admin/films/inventory/${inventoryId}`);
  }

  getMonthlyRevenue(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/dashboard/monthly-revenue`);
  }

  getTopFilms(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/reports/top-films`);
  }

  getCategoryPerformance(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/reports/category-performance`);
  }

  getTopCustomers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/reports/top-customers`);
  }

  getRevenueByRating(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/reports/revenue-by-rating`);
  }

  getRentalsByMonth(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/reports/rentals-by-month`);
  }

  getActors(): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/actors`);
  }

  createActor(data: any): Observable<any> {
    return this.http.post(`${this.url}/admin/actors`, data);
  }

  updateActor(id: number, data: any): Observable<any> {
    return this.http.put(`${this.url}/admin/actors/${id}`, data);
  }

  deleteActor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/admin/actors/${id}`);
  }

  createCategory(data: any): Observable<any> {
    return this.http.post(`${this.url}/admin/categories`, data);
  }

  updateCategory(id: number, data: any): Observable<any> {
    return this.http.put(`${this.url}/admin/categories/${id}`, data);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/admin/categories/${id}`);
  }

  updateCustomer(id: number, data: any): Observable<any> {
    return this.http.patch(`${this.url}/admin/customers/${id}`, data);
  }

  getStaffProfile(staffId: number): Observable<any> {
    return this.http.get(`${this.url}/staff/${staffId}/profile`);
  }

  getRecentRentals(limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/dashboard/recent-rentals?limit=${limit}`);
  }

  getTopActors(limit: number = 5): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/dashboard/top-actors?limit=${limit}`);
  }

  getLowInventory(threshold: number = 3): Observable<any[]> {
    return this.http.get<any[]>(`${this.url}/admin/dashboard/low-inventory?threshold=${threshold}`);
  }

  getActorStats(actorId: number): Observable<any> {
    return this.http.get(`${this.url}/admin/actors/${actorId}/stats`);
  }
}
