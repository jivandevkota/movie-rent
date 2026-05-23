import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-staff-profile',
  templateUrl: './staff-profile.component.html',
  styleUrls: ['./staff-profile.component.css']
})
export class StaffProfileComponent implements OnInit {
  profile: any = null;
  loading = true;
  error = '';

  constructor(
    private adminService: AdminService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('staff');
    if (!data) { this.router.navigate(['/staff-login']); return; }
    const staff = JSON.parse(data);
    this.adminService.getStaffProfile(staff.staffId).subscribe({
      next: (res) => { this.profile = res; this.loading = false; },
      error: () => { this.error = 'Failed to load profile.'; this.loading = false; }
    });
  }

  goBack(): void { this.router.navigate(['/admin']); }
}
