import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: any = null;
  loading = true;
  error = '';

  constructor(
    private publicService: PublicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('customer');
    if (!data) { this.router.navigate(['/login']); return; }
    const customer = JSON.parse(data);
    this.publicService.getCustomerProfile(customer.customerId).subscribe({
      next: (res) => { this.profile = res; this.loading = false; },
      error: () => { this.error = 'Failed to load profile.'; this.loading = false; }
    });
  }

  goBack(): void { this.router.navigate(['/']); }
}
