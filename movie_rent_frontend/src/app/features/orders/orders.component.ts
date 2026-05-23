import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from 'src/app/core/services/order.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  loading = true;
  error = '';
  customerName = '';
  expandedOrder: number | null = null;
  private baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const data = localStorage.getItem('customer');
    if (!data) {
      this.router.navigate(['/login']);
      return;
    }
    const customer = JSON.parse(data);
    this.customerName = customer.firstName + ' ' + customer.lastName;
    this.orderService.getOrders(customer.customerId).subscribe({
      next: (res) => {
        this.orders = this.mapImages(res);
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load orders.';
        this.loading = false;
      }
    });
  }

  private mapImages(orders: any[]): any[] {
    return orders.map((o: any) => ({
      ...o,
      items: (o.items || []).map((i: any) => ({
        ...i,
        imageUrl: this.baseUrl + i.imageUrl
      })),
      rentals: (o.rentals || []).map((r: any) => ({
        ...r,
        imageUrl: this.baseUrl + r.imageUrl
      }))
    }));
  }

  toggleOrder(orderId: number): void {
    this.expandedOrder = this.expandedOrder === orderId ? null : orderId;
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }
}
