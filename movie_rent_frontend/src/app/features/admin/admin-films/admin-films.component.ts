import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-admin-films',
  templateUrl: './admin-films.component.html',
  styleUrls: ['./admin-films.component.css']
})
export class AdminFilmsComponent implements OnInit {
  films: any[] = [];
  filteredFilms: any[] = [];
  categories: any[] = [];
  languages: any[] = [];
  loading = true;
  searchText = '';
  page = 0;
  pageSize = 20;
  totalPages = 0;
  totalElements = 0;

  // Form modal
  showFormModal = false;
  editingFilm: any = null;
  saving = false;
  formError = '';
  formData: any = {};

  // Inventory modal
  showInventoryModal = false;
  inventoryFilm: any = null;
  inventoryItems: any[] = [];
  inventoryLoading = false;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    const data = localStorage.getItem('staff');
    if (!data) return;
    this.loadData();
  }

  private loadData(): void {
    this.loading = true;
    this.adminService.getCategories().subscribe(cats => {
      this.categories = cats;
    });
    this.loadFilms();
  }

  loadFilms(): void {
    this.adminService.getFilms(this.page, this.pageSize).subscribe({
      next: (res) => {
        this.films = res.content;
        this.filteredFilms = res.content;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.loadFilms(); }
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) { this.page++; this.loadFilms(); }
  }

  filterFilms(): void {
    const t = this.searchText.toLowerCase();
    this.filteredFilms = this.films.filter(f =>
      f.title.toLowerCase().includes(t) ||
      f.categoryName?.toLowerCase().includes(t)
    );
  }

  // Form modal
  openCreateModal(): void {
    this.editingFilm = null;
    this.formData = {
      title: '', description: '', releaseYear: 2026,
      rentalDuration: 3, rentalRate: 4.99, length: 120,
      replacementCost: 19.99, rating: 'PG', specialFeatures: '',
      languageId: 1, categoryId: 1
    };
    this.formError = '';
    this.languages = [{ languageId: 1, name: 'English' }];
    this.showFormModal = true;
  }

  openEditModal(film: any): void {
    this.editingFilm = film;
    this.formData = {
      title: film.title,
      description: film.description || '',
      releaseYear: film.releaseYear,
      rentalDuration: film.rentalDuration,
      rentalRate: film.rentalRate,
      length: film.length,
      replacementCost: film.replacementCost,
      rating: film.rating || 'PG',
      specialFeatures: film.specialFeatures || '',
      languageId: film.languageId || 1,
      categoryId: film.categoryId || 1
    };
    this.formError = '';
    this.languages = [{ languageId: 1, name: 'English' }];
    this.showFormModal = true;
  }

  closeFormModal(): void {
    this.showFormModal = false;
    this.editingFilm = null;
  }

  saveFilm(): void {
    if (!this.formData.title) {
      this.formError = 'Title is required.';
      return;
    }
    this.saving = true;
    this.formError = '';

    const body = {
      title: this.formData.title,
      description: this.formData.description || '',
      releaseYear: this.formData.releaseYear || 2026,
      rentalDuration: this.formData.rentalDuration || 3,
      rentalRate: this.formData.rentalRate || 4.99,
      length: this.formData.length || null,
      replacementCost: this.formData.replacementCost || 19.99,
      rating: this.formData.rating || 'PG',
      specialFeatures: this.formData.specialFeatures || '',
      languageId: this.formData.languageId || 1,
      categoryId: this.formData.categoryId || 1
    };

    const request = this.editingFilm
      ? this.adminService.updateFilm(this.editingFilm.filmId, body)
      : this.adminService.createFilm(body);

    request.subscribe({
      next: () => {
        this.saving = false;
        this.closeFormModal();
        this.loadData();
      },
      error: (err) => {
        this.saving = false;
        this.formError = err.error?.error || 'Failed to save film.';
      }
    });
  }

  confirmDelete(film: any): void {
    if (!confirm('Delete "' + film.title + '" permanently?')) return;
    this.adminService.deleteFilm(film.filmId).subscribe({
      next: () => this.loadData()
    });
  }

  // Inventory modal
  openInventoryModal(film: any): void {
    this.inventoryFilm = film;
    this.showInventoryModal = true;
    this.inventoryLoading = true;
    this.adminService.getInventory(film.filmId).subscribe({
      next: (res) => {
        this.inventoryItems = res;
        this.inventoryLoading = false;
      },
      error: () => this.inventoryLoading = false
    });
  }

  closeInventoryModal(): void {
    this.showInventoryModal = false;
    this.inventoryFilm = null;
  }

  addCopy(): void {
    if (!this.inventoryFilm) return;
    this.adminService.addInventory(this.inventoryFilm.filmId).subscribe({
      next: (res) => {
        this.inventoryItems.push(res);
      }
    });
  }

  removeCopy(inv: any): void {
    if (!confirm('Remove copy #' + inv.inventoryId + '?')) return;
    this.adminService.removeInventory(inv.inventoryId).subscribe({
      next: () => {
        this.inventoryItems = this.inventoryItems.filter(i => i.inventoryId !== inv.inventoryId);
      }
    });
  }
}
