import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-admin-reports',
  templateUrl: './admin-reports.component.html',
  styleUrls: ['./admin-reports.component.css']
})
export class AdminReportsComponent implements OnInit {
  loading = true;
  monthlyRevenue: any[] = [];
  topFilms: any[] = [];
  categoryPerformance: any[] = [];
  topCustomers: any[] = [];
  revenueByRating: any[] = [];
  rentalsByMonth: any[] = [];
  maxCatRevenue = 0;
  maxRentalCount = 0;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    const data = localStorage.getItem('staff');
    if (!data) return;
    this.adminService.getMonthlyRevenue().subscribe(r => this.monthlyRevenue = r);
    this.adminService.getTopFilms().subscribe({
      next: (r) => {
        this.topFilms = r;
        this.maxRentalCount = Math.max(...r.map((x: any) => x.rentalCount), 1);
      }
    });
    this.adminService.getTopCustomers().subscribe(r => this.topCustomers = r);
    this.adminService.getRevenueByRating().subscribe(r => this.revenueByRating = r);
    this.adminService.getRentalsByMonth().subscribe(r => this.rentalsByMonth = r);
    this.adminService.getCategoryPerformance().subscribe({
      next: (r) => {
        this.categoryPerformance = r;
        this.maxCatRevenue = Math.max(...r.map((x: any) => parseFloat(x.revenue)), 1);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  getBarWidth(revenue: string): number {
    return (parseFloat(revenue) / this.maxCatRevenue) * 100;
  }

  getFilmBarWidth(count: number): number {
    return (count / this.maxRentalCount) * 100;
  }
}
