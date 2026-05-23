import { Component, OnInit } from '@angular/core';
import { AdminCustomerService } from 'src/app/core/services/admin-customer.service';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-admin-customers',
  templateUrl: './admin-customers.component.html',
  styleUrls: ['./admin-customers.component.css']
})
export class AdminCustomersComponent implements OnInit {
  customers: any[] = [];
  loading = true;
  selectedCustomer: any = null;
  customerRentals: any[] = [];
  rentalsLoading = false;
  page = 0;
  pageSize = 20;
  totalPages = 0;
  totalElements = 0;
  editingCustomer = false;
  editData = { firstName: '', lastName: '', email: '', active: true };
  saving = false;
  saveError = '';

  constructor(
    private adminCustomerService: AdminCustomerService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('staff');
    if (!data) return;
    this.loadCustomers();
  }

  private loadCustomers(): void {
    this.adminCustomerService.getCustomers(this.page, this.pageSize).subscribe({
      next: (res) => {
        this.customers = res.content;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.loadCustomers(); }
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadCustomers(); }
  }

  viewDetails(customer: any): void {
    this.selectedCustomer = customer;
    this.editingCustomer = false;
    this.rentalsLoading = true;
    this.adminCustomerService.getCustomerRentals(customer.customerId).subscribe({
      next: (res) => {
        this.customerRentals = res;
        this.rentalsLoading = false;
      },
      error: () => this.rentalsLoading = false
    });
  }

  closeDetails(): void {
    this.selectedCustomer = null;
    this.editingCustomer = false;
  }

  startEdit(): void {
    this.editingCustomer = true;
    this.editData = {
      firstName: this.selectedCustomer.firstName,
      lastName: this.selectedCustomer.lastName,
      email: this.selectedCustomer.email,
      active: this.selectedCustomer.active
    };
    this.saveError = '';
  }

  cancelEdit(): void {
    this.editingCustomer = false;
  }

  saveEdit(): void {
    this.saving = true;
    this.saveError = '';
    this.adminService.updateCustomer(this.selectedCustomer.customerId, this.editData).subscribe({
      next: (res) => {
        this.saving = false;
        this.selectedCustomer = { ...this.selectedCustomer, ...res };
        this.editingCustomer = false;
        this.loadCustomers();
      },
      error: (err) => {
        this.saving = false;
        this.saveError = err.error?.error || 'Failed to update.';
      }
    });
  }
}
