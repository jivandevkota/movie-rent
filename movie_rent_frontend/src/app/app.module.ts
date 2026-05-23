import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './features/home/home.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from './shared/material.module';
import { FilmDetailsComponent } from './features/film-details/film-details.component';
import { LoginComponent } from './features/auth/login/login.component';
import { MyRentalsComponent } from './features/my-rentals/my-rentals.component';
import { CartComponent } from './features/cart/cart.component';
import { OrdersComponent } from './features/orders/orders.component';
import { RegisterComponent } from './features/auth/register/register.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    FilmDetailsComponent,
    LoginComponent,
    MyRentalsComponent,
    CartComponent,
    OrdersComponent,
    RegisterComponent,
    PaymentHistoryComponent,
    StaffLoginComponent,
    StaffDashboardComponent,
    AdminFilmsComponent,
    AdminCustomersComponent,
    AdminRentalsComponent,
    AdminReportsComponent,
    ActorDetailComponent,
    AdminActorsComponent,
    ProfileComponent,
    StaffProfileComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
