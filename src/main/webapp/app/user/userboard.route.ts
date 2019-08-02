import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AccInfoComponent } from 'app/user/acc-info/acc-info.component';
import { AccJobComponent } from 'app/user/acc-job/acc-job.component';
import { AccConfigComponent } from 'app/user/acc-config/acc-config.component';
import { UserCurrentResolve } from 'app/admin';

export const UserboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    resolve: {
      user: UserCurrentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.dashboard.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'account',
    component: AccInfoComponent,
    resolve: {
      user: UserCurrentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.account.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'service',
    component: AccJobComponent,
    resolve: {
      user: UserCurrentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.service.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'setting',
    component: AccConfigComponent,
    resolve: {
      user: UserCurrentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.user.setting.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
