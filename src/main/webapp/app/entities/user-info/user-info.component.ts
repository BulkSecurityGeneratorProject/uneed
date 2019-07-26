import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserInfo } from 'app/shared/model/user-info.model';
import { AccountService } from 'app/core';
import { UserInfoService } from './user-info.service';

@Component({
  selector: 'jhi-user-info',
  templateUrl: './user-info.component.html'
})
export class UserInfoComponent implements OnInit, OnDestroy {
  userInfos: IUserInfo[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected userInfoService: UserInfoService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.userInfoService
      .query()
      .pipe(
        filter((res: HttpResponse<IUserInfo[]>) => res.ok),
        map((res: HttpResponse<IUserInfo[]>) => res.body)
      )
      .subscribe(
        (res: IUserInfo[]) => {
          this.userInfos = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUserInfos();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUserInfo) {
    return item.id;
  }

  registerChangeInUserInfos() {
    this.eventSubscriber = this.eventManager.subscribe('userInfoListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
