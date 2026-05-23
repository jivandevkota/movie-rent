import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';
import { CartService } from 'src/app/core/services/cart.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-film-details',
  templateUrl: './film-details.component.html',
  styleUrls: ['./film-details.component.css']
})
export class FilmDetailsComponent implements OnInit {
  film: any = null;
  loading = true;
  quantity = 1;
  rentMsg = '';
  rentSuccess = false;
  rentLoading = false;
  cartMsg = '';
  cartLoading = false;
  cartSuccess = false;

  private readonly baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicService: PublicService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) this.loadFilmDetails(id);
  }

  loadFilmDetails(id: number): void {
    this.loading = true;
    this.publicService.getFilmDetails(id).subscribe({
      next: (res) => {
        this.film = {
          ...res,
          imageUrl: this.baseUrl + res.imageUrl,
          actors: res.actors?.map((a: any) => ({
            ...a,
            imageUrl: this.baseUrl + a.imageUrl
          })) || []
        };
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  goBack(): void { this.router.navigate(['/']); }
  goToMyRentals(): void { this.router.navigate(['/my-rentals']); }

  addToCart(): void {
    const data = localStorage.getItem('customer');
    if (!data) {
      this.router.navigate(['/login']);
      return;
    }
    const customer = JSON.parse(data);
    this.cartLoading = true;
    this.cartMsg = '';
    this.cartService.addToCart(customer.customerId, this.film.filmId, this.quantity).subscribe({
      next: () => {
        this.cartSuccess = true;
        this.cartMsg = '"' + this.film.title + '" added to cart!';
        this.cartLoading = false;
      },
      error: (err) => {
        this.cartSuccess = false;
        this.cartMsg = err.error?.error || 'Failed to add to cart.';
        this.cartLoading = false;
      }
    });
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('customer') !== null;
  }

  rentFilm(): void {
    const data = localStorage.getItem('customer');
    if (!data) {
      this.router.navigate(['/login']);
      return;
    }
    const customer = JSON.parse(data);

    this.rentLoading = true;
    this.rentMsg = '';

    this.publicService.rentFilm(customer.customerId, this.film.filmId).subscribe({
      next: (res) => {
        this.rentSuccess = true;
        this.rentMsg = '"' + this.film.title + '" rented successfully!';
        this.rentLoading = false;
      },
      error: (err) => {
        this.rentSuccess = false;
        this.rentMsg = err.error?.error || 'Failed to rent film.';
        this.rentLoading = false;
      }
    });
  }
}
