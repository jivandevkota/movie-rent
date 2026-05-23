import { Component, OnInit } from '@angular/core';
import { PublicService } from 'src/app/core/services/public.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  films: any[] = [];
  categories: any[] = [];
  popularFilms: any[] = [];

  page = 0;
  totalPages = 0;
  totalElements = 0;

  searchText = '';
  gotoPageNumber = 1;
  selectedCategory: number | null = null;
  currentMode: 'all' | 'category' | 'search' = 'all';

  private readonly baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private publicService: PublicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFilms();
    this.loadCategories();
    this.loadPopular();
  }

  loadFilms(): void {
    this.currentMode = 'all';
    this.selectedCategory = null;
    this.page = 0;
    this.gotoPageNumber = 1;

    this.publicService.getFilms(this.page).subscribe(res => {
      this.films = this.mapImages(res.content);
      this.totalPages = res.totalPages;
      this.totalElements = res.totalElements;
    });
  }

  loadCategories(): void {
    this.publicService.getAllCategory().subscribe(res => {
      this.categories = res;
    });
  }

  loadPopular(): void {
    this.publicService.getPopularFilms().subscribe(res => {
      this.popularFilms = res.map((f: any) => ({
        ...f,
        imageUrl: this.baseUrl + f.imageUrl
      }));
    });
  }

  loadByCategory(categoryId: number): void {
    this.currentMode = 'category';
    this.selectedCategory = categoryId;
    this.page = 0;
    this.gotoPageNumber = 1;

    this.publicService.getByCategory(categoryId, this.page).subscribe(res => {
      this.films = this.mapImages(res.content);
      this.totalPages = res.totalPages;
      this.totalElements = res.totalElements;
    });
  }

  searchFilms(): void {
    if (!this.searchText.trim()) {
      this.loadFilms();
      return;
    }
    this.currentMode = 'search';
    this.page = 0;
    this.gotoPageNumber = 1;

    this.publicService.getBySearch(this.searchText, this.page).subscribe(res => {
      this.films = this.mapImages(res.content);
      this.totalPages = res.totalPages;
      this.totalElements = res.totalElements;
    });
  }

  reloadData(): void {
    if (this.currentMode === 'all') {
      this.publicService.getFilms(this.page).subscribe(res => {
        this.films = this.mapImages(res.content);
      });
    } else if (this.currentMode === 'category' && this.selectedCategory) {
      this.publicService.getByCategory(this.selectedCategory, this.page).subscribe(res => {
        this.films = this.mapImages(res.content);
      });
    } else {
      this.publicService.getBySearch(this.searchText, this.page).subscribe(res => {
        this.films = this.mapImages(res.content);
      });
    }
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.gotoPageNumber = this.page + 1;
      this.reloadData();
    }
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.gotoPageNumber = this.page + 1;
      this.reloadData();
    }
  }

  firstPage(): void {
    this.page = 0;
    this.gotoPageNumber = 1;
    this.reloadData();
  }

  lastPage(): void {
    this.page = this.totalPages - 1;
    this.gotoPageNumber = this.totalPages;
    this.reloadData();
  }

  goToPage(): void {
    if (this.gotoPageNumber >= 1 && this.gotoPageNumber <= this.totalPages) {
      this.page = this.gotoPageNumber - 1;
      this.reloadData();
    }
  }

  openFilmDetails(filmId: number): void {
    this.router.navigate(['/film', filmId]);
  }

  getCategoryName(id: number): string {
    const c = this.categories.find(c => c.id === id);
    return c ? c.name : 'Category';
  }

  categoryStyle(cat: string): any {
    const colors: Record<string, [string, string]> = {
      'Action': ['#fff0e0', '#c55'],
      'Animation': ['#e0f5ff', '#3a8abf'],
      'Children': ['#fff5e0', '#c90'],
      'Classics': ['#f0e8d8', '#886'],
      'Comedy': ['#f0ffe0', '#5a5'],
      'Documentary': ['#e8e8f8', '#66a'],
      'Drama': ['#fce8f0', '#b5a'],
      'Family': ['#e0ffe8', '#3a8'],
      'Foreign': ['#f0eaff', '#86a'],
      'Games': ['#fff0e8', '#c74'],
      'Horror': ['#fee8e8', '#c44'],
      'Music': ['#f0e8ff', '#86c'],
      'New': ['#e8f8fe', '#38c'],
      'Sci-Fi': ['#e0f0f8', '#3a8a'],
      'Sports': ['#f0f8e0', '#684'],
      'Travel': ['#e8f5f0', '#388'],
    };
    const key = Object.keys(colors).find(k => cat?.toLowerCase().includes(k.toLowerCase()));
    const [bg, color] = key ? colors[key] : ['#f0f0f0', '#888'];
    return { 'background': bg, 'color': color };
  }

  private mapImages(films: any[]): any[] {
    return films.map(f => ({ ...f, imageUrl: this.baseUrl + f.imageUrl }));
  }
}