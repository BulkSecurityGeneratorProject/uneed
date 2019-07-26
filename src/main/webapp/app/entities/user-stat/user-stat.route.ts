import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserStat } from 'app/shared/model/user-stat.model';
import { UserStatService } from './user-stat.service';
import { UserStatComponent } from './user-stat.component';
import { UserStatDetailComponent } from './user-stat-detail.component';
import { UserStatUpdateComponent } from './user-stat-update.component';
import { UserStatDeletePopupComponent } from './user-stat-delete-dialog.component';
import { IUserStat } from 'app/shared/model/user-stat.model';

@Injectable({ providedIn: 'root' })
export class UserStatResolve implements Resolve<IUserStat> {
  constructor(private service: UserStatService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserStat> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserStat>) => response.ok),
        map((userStat: HttpResponse<UserStat>) => userStat.body)
      );
    }
    return of(new UserStat());
  }
}

export const userStatRoute: Routes = [
  {
    path: '',
    component: UserStatComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userStat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserStatDetailComponent,
    resolve: {
      userStat: UserStatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userStat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserStatUpdateComponent,
    resolve: {
      userStat: UserStatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userStat.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserStatUpdateComponent,
    resolve: {
      userStat: UserStatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userStat.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userStatPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserStatDeletePopupComponent,
    resolve: {
      userStat: UserStatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userStat.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
