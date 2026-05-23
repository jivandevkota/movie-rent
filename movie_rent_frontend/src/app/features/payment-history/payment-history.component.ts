import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentService } from 'src/app/core/services/payment.service';

@Component({
  selector: 'app-payment-history',
  templateUrl: './payment-history.component.html',
  styleUrls: ['./payment-history.component.css']
})
export class PaymentHistoryComponent implements OnInit {
  payments: any[] = [];
  loading = true;
  error = '';

  constructor(
    private paymentService: PaymentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('customer');
    if (!data) {
      this.router.navigate(['/login']);
      return;
    }
    const customer = JSON.parse(data);
    this.paymentService.getPayments(customer.customerId).subscribe({
      next: (res) => {
        this.payments = res;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load payment history.';
        this.loading = false;
      }
    });
  }

  getTotal(): string {
    const total = this.payments.reduce((sum: number, p: any) => sum + p.amount, 0);
    return total.toFixed(2);
  }

  getLatestDate(): string {
    if (this.payments.length === 0) return '-';
    const latest = new Date(this.payments[0].paymentDate);
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    return months[latest.getMonth()] + ' ' + latest.getDate();
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}