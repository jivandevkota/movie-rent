import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';

export const staffGuard: CanActivateFn = () => {
  const router = inject(Router);
  if (localStorage.getItem('staff')) return true;
  router.navigate(['/staff-login']);
  return false;
};

export const customerGuard: CanActivateFn = () => {
  const router = inject(Router);
  if (localStorage.getItem('customer')) return true;
  router.navigate(['/login']);
  return false;
};

export const guestGuard: CanActivateFn = () => {
  const router = inject(Router);
  if (localStorage.getItem('customer')) {
    router.navigate(['/']);
    return false;
  }
  if (localStorage.getItem('staff')) {
    router.navigate(['/admin']);
    return false;
  }
  return true;
};
