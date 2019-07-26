import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserStat } from 'app/shared/model/user-stat.model';
import { AccountService } from 'app/core';
import { UserStatService } from './user-stat.service';

@Component({
  selector: 'jhi-user-stat',
  templateUrl: './user-stat.component.html'
})
export class UserStatComponent implements OnInit, OnDestroy {
  userStats: IUserStat[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected userStatService: UserStatService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.userStatService
      .query()
      .pipe(
        filter((res: HttpResponse<IUserStat[]>) => res.ok),
        map((res: HttpResponse<IUserStat[]>) => res.body)
      )
      .subscribe(
        (res: IUserStat[]) => {
          this.userStats = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUserStats();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUserStat) {
    return item.id;
  }

  registerChangeInUserStats() {
    this.eventSubscriber = this.eventManager.subscribe('userStatListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
