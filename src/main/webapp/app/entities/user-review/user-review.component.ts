import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserReview } from 'app/shared/model/user-review.model';
import { AccountService } from 'app/core';
import { UserReviewService } from './user-review.service';

@Component({
  selector: 'jhi-user-review',
  templateUrl: './user-review.component.html'
})
export class UserReviewComponent implements OnInit, OnDestroy {
  userReviews: IUserReview[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected userReviewService: UserReviewService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.userReviewService
      .query()
      .pipe(
        filter((res: HttpResponse<IUserReview[]>) => res.ok),
        map((res: HttpResponse<IUserReview[]>) => res.body)
      )
      .subscribe(
        (res: IUserReview[]) => {
          this.userReviews = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUserReviews();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUserReview) {
    return item.id;
  }

  registerChangeInUserReviews() {
    this.eventSubscriber = this.eventManager.subscribe('userReviewListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
