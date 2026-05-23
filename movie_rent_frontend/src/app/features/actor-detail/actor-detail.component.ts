import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicService } from 'src/app/core/services/public.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-actor-detail',
  templateUrl: './actor-detail.component.html',
  styleUrls: ['./actor-detail.component.css']
})
export class ActorDetailComponent {
  actor: any = null;
  loading = true;

  private readonly baseUrl = environment.apiUrl.replace('/api', '');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicService: PublicService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) this.loadActor(id);
  }

  loadActor(id: number): void {
    this.loading = true;
    this.publicService.getActor(id).subscribe({
      next: (res) => {
        this.actor = {
          ...res,
          imageUrl: this.baseUrl + res.imageUrl,
          films: res.films?.map((f: any) => ({
            ...f,
            imageUrl: this.baseUrl + f.imageUrl
          })) || []
        };
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.router.navigate(['/']);
      }
    });
  }

  goBack(): void {
    window.history.back();
  }
}
