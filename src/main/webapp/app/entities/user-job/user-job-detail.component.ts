import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserJob } from 'app/shared/model/user-job.model';
import { AccountService } from 'app/core';

@Component({
  selector: 'jhi-user-job-detail',
  templateUrl: './user-job-detail.component.html'
})
export class UserJobDetailComponent implements OnInit {
  userJob: IUserJob;

  constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userJob }) => {
      this.userJob = userJob;
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  previousState() {
    window.history.back();
  }
}
