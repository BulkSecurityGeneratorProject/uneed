import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AccInfoComponent } from 'app/user/acc-info/acc-info.component';
import { AccJobComponent } from 'app/user/acc-job/acc-job.component';
import { AccConfigComponent } from 'app/user/acc-config/acc-config.component';

export const UserboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.dashboard.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'account',
    component: AccInfoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.account.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'service',
    component: AccJobComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.service.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'setting',
    component: AccConfigComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.setting.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
