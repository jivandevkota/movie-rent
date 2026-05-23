import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-my-rentals',
  templateUrl: './my-rentals.component.html',
  styleUrls: ['./my-rentals.component.css']
})
export class MyRentalsComponent implements OnInit {

  rentals: any[] = [];
  activeRentals: any[] = [];
  pastRentals: any[] = [];
  loading = true;
  error = '';
  customerName = '';
  returningId: number | null = null;
  private customerId: number = 0;
  private baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private publicService: PublicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('customer');
    if (!data) {
      this.router.navigate(['/login']);
      return;
    }
    const customer = JSON.parse(data);
    this.customerId = customer.customerId;
    this.customerName = customer.firstName + ' ' + customer.lastName;
    this.loadRentals();
  }

  private loadRentals(): void {
    this.loading = true;
    this.publicService.getCustomerRentals(this.customerId).subscribe({
      next: (res) => {
        this.rentals = res.map((r: any) => ({
          ...r,
          imageUrl: this.baseUrl + '/img/films/' + r.filmId + '.jpg'
        }));
        this.activeRentals = this.rentals.filter((r: any) => r.active);
        this.pastRentals = this.rentals.filter((r: any) => !r.active);
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load rentals.';
        this.loading = false;
      }
    });
  }

  returnFilm(rentalId: number): void {
    this.returningId = rentalId;
    this.publicService.returnFilm(this.customerId, rentalId).subscribe({
      next: () => {
        this.returningId = null;
        this.loadRentals();
      },
      error: (err) => {
        this.returningId = null;
        this.error = err.error?.error || 'Failed to return film.';
      }
    });
  }

  daysSince(date: string): number {
    const diff = Date.now() - new Date(date).getTime();
    return Math.floor(diff / (1000 * 60 * 60 * 24));
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
