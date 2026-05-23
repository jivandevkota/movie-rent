import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-staff-dashboard',
  templateUrl: './staff-dashboard.component.html',
  styleUrls: ['./staff-dashboard.component.css']
})
export class StaffDashboardComponent implements OnInit {
  stats: any = {};
  recentRentals: any[] = [];
  topActors: any[] = [];
  lowInventory: any[] = [];
  monthlyRevenue: any[] = [];
  maxRevenue = 0;
  loading = true;
  staffName = '';

  constructor(
    private adminService: AdminService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('staff');
    if (!data) {
      this.router.navigate(['/staff-login']);
      return;
    }
    const staff = JSON.parse(data);
    this.staffName = staff.firstName + ' ' + staff.lastName;
    this.loadAll();
  }

  private loadAll(): void {
    this.adminService.getDashboardStats().subscribe(r => this.stats = r);
    this.adminService.getRecentRentals(6).subscribe(r => this.recentRentals = r);
    this.adminService.getTopActors(5).subscribe(r => this.topActors = r);
    this.adminService.getLowInventory(0).subscribe(r => this.lowInventory = r);
    this.adminService.getMonthlyRevenue().subscribe({
      next: (r) => {
        this.monthlyRevenue = r.slice(0, 6);
        this.maxRevenue = Math.max(...r.map((x: any) => parseFloat(x.revenue)), 1);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  getBarWidth(revenue: string): number {
    return (parseFloat(revenue) / this.maxRevenue) * 100;
  }
}
