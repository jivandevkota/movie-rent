import { Component, OnInit } from '@angular/core';
import { AdminRentalService } from 'src/app/core/services/admin-rental.service';
import { AdminCustomerService } from 'src/app/core/services/admin-customer.service';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-admin-rentals',
  templateUrl: './admin-rentals.component.html',
  styleUrls: ['./admin-rentals.component.css']
})
export class AdminRentalsComponent implements OnInit {
  rentals: any[] = [];
  filteredRentals: any[] = [];
  loading = true;
  filter = 'all';
  returningId: number | null = null;
  page = 0;
  pageSize = 20;
  totalPages = 0;
  totalElements = 0;

  showRentalModal = false;
  customers: any[] = [];
  films: any[] = [];
  selectedCustomerId: number | null = null;
  selectedFilmId: number | null = null;
  renting = false;
  rentalError = '';
  rentalSuccess = '';

  constructor(
    private adminRentalService: AdminRentalService,
    private adminCustomerService: AdminCustomerService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('staff');
    if (!data) return;
    this.loadRentals();
  }

  private loadRentals(): void {
    this.adminRentalService.getAllRentals(this.page, this.pageSize, this.filter).subscribe({
      next: (res) => {
        this.rentals = res.content;
        this.filteredRentals = res.content;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.loadRentals(); }
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadRentals(); }
  }

  filterChanged(newFilter: string): void {
    this.filter = newFilter;
    this.page = 0;
    this.loadRentals();
  }

  processReturn(rental: any): void {
    if (!confirm('Process return for "' + rental.filmTitle + '" by ' + rental.customerName + '?')) return;
    this.returningId = rental.rentalId;
    this.adminRentalService.forceReturn(rental.rentalId).subscribe({
      next: () => {
        this.returningId = null;
        this.loadRentals();
      },
      error: () => this.returningId = null
    });
  }

  openRentalModal(): void {
    this.showRentalModal = true;
    this.selectedCustomerId = null;
    this.selectedFilmId = null;
    this.rentalError = '';
    this.rentalSuccess = '';
    this.renting = false;

    this.adminCustomerService.getCustomers(0, 500).subscribe({
      next: (res) => {
        this.customers = res.content.map((c: any) => ({ id: c.customerId, name: c.firstName + ' ' + c.lastName }));
      }
    });

    this.adminService.getFilms(0, 500).subscribe({
      next: (res) => {
        this.films = res.content.map((f: any) => ({ id: f.filmId, title: f.title }));
      }
    });
  }

  closeRentalModal(): void {
    this.showRentalModal = false;
  }

  submitRental(): void {
    if (!this.selectedCustomerId || !this.selectedFilmId) return;
    this.renting = true;
    this.rentalError = '';
    this.rentalSuccess = '';

    this.adminRentalService.processRental(this.selectedCustomerId, this.selectedFilmId).subscribe({
      next: () => {
        this.renting = false;
        this.rentalSuccess = 'Film rented successfully!';
        this.loadRentals();
        setTimeout(() => this.closeRentalModal(), 1500);
      },
      error: () => {
        this.renting = false;
        this.rentalError = 'Failed to process rental. Film may be out of stock.';
      }
    });
  }
}
