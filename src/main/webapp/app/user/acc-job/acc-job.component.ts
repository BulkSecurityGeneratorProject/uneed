import { Component, OnInit } from '@angular/core';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { IUserJob } from 'app/shared/model/user-job.model';
import { UserboardService } from '../userboard.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-myservice',
  templateUrl: './acc-job.component.html',
  styleUrls: ['./acc-job.component.scss']
})
export class AccJobComponent implements OnInit {
  userInfo: IUserInfo;

  userJob: IUserJob;

  constructor(protected dashboardService: UserboardService, protected jhiAlertService: JhiAlertService) {}

  ngOnInit() {}

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
