import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from 'src/app/core/services/cart.service';
import { OrderService } from 'src/app/core/services/order.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: any = null;
  loading = true;
  error = '';
  opError = '';
  checkingOut = false;
  checkoutSuccess = false;
  checkoutMsg = '';
  customerName = '';
  customerEmail = '';
  private baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private cartService: CartService,
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
    this.customerEmail = customer.email;
    this.loadCart(customer.customerId);
  }

  private get customerId(): number {
    return JSON.parse(localStorage.getItem('customer')!).customerId;
  }

  loadCart(customerId: number): void {
    this.loading = true;
    this.error = '';
    this.cartService.getCart(customerId).subscribe({
      next: (res) => {
        this.cart = this.mapImages(res);
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load cart.';
        this.loading = false;
      }
    });
  }

  private mapImages(cart: any): any {
    if (!cart || !cart.items) return cart;
    return {
      ...cart,
      items: cart.items.map((i: any) => ({
        ...i,
        imageUrl: this.baseUrl + i.imageUrl
      }))
    };
  }

  updateQuantity(item: any, delta: number): void {
    const newQty = item.quantity + delta;
    if (newQty < 1) return;
    this.cartService.updateItem(this.customerId, item.cartItemId, newQty).subscribe({
      next: (res) => { this.cart = this.mapImages(res); this.opError = ''; },
      error: () => { this.opError = 'Failed to update quantity.'; }
    });
  }

  removeItem(item: any): void {
    this.cartService.removeItem(this.customerId, item.cartItemId).subscribe({
      next: (res) => { this.cart = this.mapImages(res); this.opError = ''; },
      error: () => { this.opError = 'Failed to remove item.'; }
    });
  }

  checkout(): void {
    this.checkingOut = true;
    this.checkoutMsg = '';
    this.orderService.checkout(this.customerId).subscribe({
      next: (res) => {
        this.checkingOut = false;
        this.checkoutSuccess = true;
        this.checkoutMsg = 'Order placed successfully! ' + res.rentals.length + ' film(s) rented.';
        this.cart = { items: [], totalAmount: 0, itemCount: 0 };
        setTimeout(() => this.router.navigate(['/orders']), 2000);
      },
      error: (err) => {
        this.checkingOut = false;
        this.checkoutSuccess = false;
        this.checkoutMsg = err.error?.error || 'Checkout failed. Please try again.';
      }
    });
  }

  continueShopping(): void {
    this.router.navigate(['/']);
  }

  viewOrders(): void {
    this.router.navigate(['/orders']);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
