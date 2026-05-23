import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { FilmDetailsComponent } from './features/film-details/film-details.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { MyRentalsComponent } from './features/my-rentals/my-rentals.component';
import { CartComponent } from './features/cart/cart.component';
import { OrdersComponent } from './features/orders/orders.component';
import { PaymentHistoryComponent } from './features/payment-history/payment-history.component';
import { StaffLoginComponent } from './features/staff/staff-login/staff-login.component';
import { StaffDashboardComponent } from './features/staff/staff-dashboard/staff-dashboard.component';
import { StaffProfileComponent } from './features/staff/staff-profile/staff-profile.component';
import { AdminFilmsComponent } from './features/admin/admin-films/admin-films.component';
import { AdminCustomersComponent } from './features/admin/admin-customers/admin-customers.component';
import { AdminRentalsComponent } from './features/admin/admin-rentals/admin-rentals.component';
import { AdminReportsComponent } from './features/admin/admin-reports/admin-reports.component';
import { ActorDetailComponent } from './features/actor-detail/actor-detail.component';
import { AdminActorsComponent } from './features/admin/admin-actors/admin-actors.component';
import { ProfileComponent } from './features/profile/profile.component';
import { staffGuard, customerGuard, guestGuard } from './core/guards/auth.guard';

const routes: Routes = [
  {path:'login',component:LoginComponent, canActivate:[guestGuard]},
  {path:'register',component:RegisterComponent, canActivate:[guestGuard]},
  {path:'staff-login',component:StaffLoginComponent, canActivate:[guestGuard]},
  {path:'admin',component:StaffDashboardComponent, canActivate:[staffGuard]},
  {path:'admin/profile',component:StaffProfileComponent, canActivate:[staffGuard]},
  {path:'admin/films',component:AdminFilmsComponent, canActivate:[staffGuard]},
  {path:'admin/customers',component:AdminCustomersComponent, canActivate:[staffGuard]},
  {path:'admin/rentals',component:AdminRentalsComponent, canActivate:[staffGuard]},
  {path:'admin/reports',component:AdminReportsComponent, canActivate:[staffGuard]},
  {path:'admin/actors',component:AdminActorsComponent, canActivate:[staffGuard]},
  {path:'profile',component:ProfileComponent, canActivate:[customerGuard]},
  {path:'my-rentals',component:MyRentalsComponent, canActivate:[customerGuard]},
  {path:'cart',component:CartComponent, canActivate:[customerGuard]},
  {path:'orders',component:OrdersComponent, canActivate:[customerGuard]},
  {path:'payments',component:PaymentHistoryComponent, canActivate:[customerGuard]},
  {path:'',component:HomeComponent},
  {path:'film/:id',component:FilmDetailsComponent},
  {path:'actor/:id',component:ActorDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
