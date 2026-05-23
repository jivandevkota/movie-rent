import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private router: Router) {}

  get isLoggedIn(): boolean {
    return localStorage.getItem('customer') !== null || localStorage.getItem('staff') !== null;
  }

  get isCustomer(): boolean {
    return localStorage.getItem('customer') !== null;
  }

  get isStaff(): boolean {
    return localStorage.getItem('staff') !== null;
  }

  get loggedInName(): string {
    const staff = localStorage.getItem('staff');
    if (staff) {
      try { return JSON.parse(staff).firstName || ''; } catch { return ''; }
    }
    const customer = localStorage.getItem('customer');
    if (customer) {
      try { return JSON.parse(customer).firstName || ''; } catch { return ''; }
    }
    return '';
  }

  logout(): void {
    localStorage.removeItem('customer');
    localStorage.removeItem('staff');
    this.router.navigate(['/']);
  }
}
