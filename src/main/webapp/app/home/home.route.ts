import { Routes } from '@angular/router';

import { HomeComponent } from './';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'home.title',
      defaultSort: 'id,asc'
    }
  },
  {
    path: 'dashboard',
    loadChildren: '../dashboard/dashboard.module#DashboardModule'
  }
];
