import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = '';
  password = '';
  error = '';
  loading = false;

  constructor(
    private publicService: PublicService,
    private router: Router
  ) {}

  onLogin(): void {
    if (!this.email || !this.password) {
      this.error = 'Please enter both email and password';
      return;
    }

    this.loading = true;
    this.error = '';

    this.publicService.login(this.email, this.password).subscribe({
      next: (res) => {
        localStorage.setItem('customer', JSON.stringify(res));
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.error || 'Login failed. Check your credentials.';
      }
    });
  }
}
