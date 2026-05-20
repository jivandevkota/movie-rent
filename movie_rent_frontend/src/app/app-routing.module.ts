import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { FilmDetailsComponent } from './features/film-details/film-details.component';

const routes: Routes = [
  {path:'',component:HomeComponent},
  {path:'film/:id',component:FilmDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
