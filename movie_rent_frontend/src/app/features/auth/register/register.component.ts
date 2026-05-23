import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  loading = false;
  error = '';
  success = false;

  constructor(
    private publicService: PublicService,
    private router: Router
  ) {}

  register(): void {
    this.error = '';
    this.loading = true;
    this.publicService.register(this.firstName, this.lastName, this.email, this.password).subscribe({
      next: (res) => {
        this.loading = false;
        this.success = true;
        localStorage.setItem('customer', JSON.stringify(res));
        setTimeout(() => this.router.navigate(['/']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.error || 'Registration failed.';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}