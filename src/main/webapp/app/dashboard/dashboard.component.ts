import { Component, OnInit } from '@angular/core';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { IUserJob } from 'app/shared/model/user-job.model';
import { DashboardService } from './dashboard.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  userInfo: IUserInfo;

  userJob: IUserJob;

  constructor(protected dashboardService: DashboardService, protected jhiAlertService: JhiAlertService) {}

  ngOnInit() {
    this.dashboardService
      .currentJob()
      .subscribe((res: HttpResponse<IUserJob[]>) => this.currentJob(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  private currentJob(data: IUserJob[]) {
    if (data.length > 0) {
      this.userJob = data[0];
      console.log(this.userJob);
    }
  }
}
