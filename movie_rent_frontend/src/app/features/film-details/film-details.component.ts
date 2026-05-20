import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-film-details',
  templateUrl: './film-details.component.html',
  styleUrls: ['./film-details.component.css']
})
export class FilmDetailsComponent {
  film: any = null;
  loading = true;
  quantity = 1;

  private readonly baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicService: PublicService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) this.loadFilmDetails(id);
  }

  loadFilmDetails(id: number): void {
    this.loading = true;
    this.publicService.getFilmDetails(id).subscribe({
      next: (res) => {
        this.film = { ...res, imageUrl: this.baseUrl + res.imageUrl };
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  goBack(): void { this.router.navigate(['/']); }
  addToCart(): void { console.log('Add to cart', this.film.filmId); }
}
