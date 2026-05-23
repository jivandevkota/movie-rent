import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { StaffService } from 'src/app/core/services/staff.service';

@Component({
  selector: 'app-staff-login',
  templateUrl: './staff-login.component.html',
  styleUrls: ['./staff-login.component.css']
})
export class StaffLoginComponent {
  email = '';
  password = '';
  loading = false;
  error = '';

  constructor(
    private staffService: StaffService,
    private router: Router
  ) {}

  onLogin(): void {
    if (!this.email || !this.password) {
      this.error = 'Please enter email and password.';
      return;
    }
    this.loading = true;
    this.error = '';
    this.staffService.login(this.email, this.password).subscribe({
      next: (res) => {
        localStorage.setItem('staff', JSON.stringify(res));
        this.loading = false;
        this.router.navigate(['/admin']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.error || 'Login failed.';
      }
    });
  }
}
