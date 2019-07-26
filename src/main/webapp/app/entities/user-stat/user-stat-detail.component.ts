import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserStat } from 'app/shared/model/user-stat.model';

@Component({
  selector: 'jhi-user-stat-detail',
  templateUrl: './user-stat-detail.component.html'
})
export class UserStatDetailComponent implements OnInit {
  userStat: IUserStat;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userStat }) => {
      this.userStat = userStat;
    });
  }

  previousState() {
    window.history.back();
  }
}
