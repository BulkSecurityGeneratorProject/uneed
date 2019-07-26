import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserReview } from 'app/shared/model/user-review.model';
import { UserReviewService } from './user-review.service';
import { UserReviewComponent } from './user-review.component';
import { UserReviewDetailComponent } from './user-review-detail.component';
import { UserReviewUpdateComponent } from './user-review-update.component';
import { UserReviewDeletePopupComponent } from './user-review-delete-dialog.component';
import { IUserReview } from 'app/shared/model/user-review.model';

@Injectable({ providedIn: 'root' })
export class UserReviewResolve implements Resolve<IUserReview> {
  constructor(private service: UserReviewService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserReview> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserReview>) => response.ok),
        map((userReview: HttpResponse<UserReview>) => userReview.body)
      );
    }
    return of(new UserReview());
  }
}

export const userReviewRoute: Routes = [
  {
    path: '',
    component: UserReviewComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userReview.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserReviewDetailComponent,
    resolve: {
      userReview: UserReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userReview.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserReviewUpdateComponent,
    resolve: {
      userReview: UserReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userReview.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserReviewUpdateComponent,
    resolve: {
      userReview: UserReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userReview.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userReviewPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserReviewDeletePopupComponent,
    resolve: {
      userReview: UserReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'uneedApp.userReview.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
