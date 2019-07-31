import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserJob } from 'app/shared/model/user-job.model';
import { UserJobService } from './user-job.service';
import { UserJobComponent } from './user-job.component';
import { UserJobDetailComponent } from './user-job-detail.component';
import { UserJobUpdateComponent } from './user-job-update.component';
import { UserJobDeletePopupComponent } from './user-job-delete-dialog.component';
import { IUserJob } from 'app/shared/model/user-job.model';

@Injectable({ providedIn: 'root' })
export class UserJobResolve implements Resolve<IUserJob> {
  constructor(private service: UserJobService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserJob> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserJob>) => response.ok),
        map((userJob: HttpResponse<UserJob>) => userJob.body)
      );
    }
    return of(new UserJob());
  }
}

export const userJobRoute: Routes = [
  {
    path: '',
    component: UserJobComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'uneedApp.userJob.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserJobDetailComponent,
    resolve: {
      userJob: UserJobResolve
    },
    data: {
      authorities: [],
      pageTitle: 'uneedApp.userJob.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserJobUpdateComponent,
    resolve: {
      userJob: UserJobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userJob.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserJobUpdateComponent,
    resolve: {
      userJob: UserJobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userJob.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userJobPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserJobDeletePopupComponent,
    resolve: {
      userJob: UserJobResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userJob.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
