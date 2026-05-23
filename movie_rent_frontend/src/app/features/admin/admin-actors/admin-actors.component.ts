import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-admin-actors',
  templateUrl: './admin-actors.component.html',
  styleUrls: ['./admin-actors.component.css']
})
export class AdminActorsComponent implements OnInit {
  actors: any[] = [];
  loading = true;
  searchText = '';
  filteredActors: any[] = [];

  showForm = false;
  editing: any = null;
  formData = { firstName: '', lastName: '' };
  saving = false;
  formError = '';

  selectedActor: any = null;
  actorStats: any = null;
  statsLoading = false;

  private baseUrl = environment.apiUrl.replace('/api', '');

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    if (!localStorage.getItem('staff')) return;
    this.loadActors();
  }

  loadActors(): void {
    this.loading = true;
    this.adminService.getActors().subscribe({
      next: (res) => {
        this.actors = this.mapImages(res);
        this.filteredActors = this.actors;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  private mapImages(actors: any[]): any[] {
    return actors.map(a => ({
      ...a,
      imageUrl: this.baseUrl + '/img/actors/' + a.actorId + '.jpg'
    }));
  }

  filterActors(): void {
    const t = this.searchText.toLowerCase();
    this.filteredActors = this.actors.filter(a =>
      a.name.toLowerCase().includes(t)
    );
  }

  viewActor(actor: any): void {
    this.selectedActor = actor;
    this.statsLoading = true;
    this.actorStats = null;
    this.adminService.getActorStats(actor.actorId).subscribe({
      next: (res) => {
        this.actorStats = {
          ...res,
          films: (res.topFilms || []).map((f: any) => ({
            ...f,
            imageUrl: this.baseUrl + '/img/films/' + f.filmId + '.jpg'
          }))
        };
        this.statsLoading = false;
      },
      error: () => this.statsLoading = false
    });
  }

  closeView(): void {
    this.selectedActor = null;
    this.actorStats = null;
  }

  openCreate(): void {
    this.editing = null;
    this.formData = { firstName: '', lastName: '' };
    this.formError = '';
    this.showForm = true;
  }

  openEdit(actor: any): void {
    const parts = actor.name.split(' ');
    this.editing = actor;
    this.formData = {
      firstName: parts[0] || '',
      lastName: parts.slice(1).join(' ') || ''
    };
    this.formError = '';
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.editing = null;
  }

  save(): void {
    if (!this.formData.firstName || !this.formData.lastName) {
      this.formError = 'First and last name required.';
      return;
    }
    this.saving = true;
    this.formError = '';
    const req = this.editing
      ? this.adminService.updateActor(this.editing.actorId, this.formData)
      : this.adminService.createActor(this.formData);
    req.subscribe({
      next: () => {
        this.saving = false;
        this.closeForm();
        this.loadActors();
      },
      error: (err) => {
        this.saving = false;
        this.formError = err.error?.error || 'Failed to save.';
      }
    });
  }

  confirmDelete(actor: any): void {
    if (!confirm(`Delete actor "${actor.name}"?`)) return;
    this.adminService.deleteActor(actor.actorId).subscribe({
      next: () => this.loadActors()
    });
  }
}
