import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserJob } from 'app/shared/model/user-job.model';

@Component({
  selector: 'jhi-user-job-detail',
  templateUrl: './user-job-detail.component.html'
})
export class UserJobDetailComponent implements OnInit {
  userJob: IUserJob;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userJob }) => {
      this.userJob = userJob;
    });
  }

  previousState() {
    window.history.back();
  }
}
