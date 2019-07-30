import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { DashboardComponent } from './dashboard.component';

export const DashboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.dashboard.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
