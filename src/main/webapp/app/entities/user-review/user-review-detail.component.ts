import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserReview } from 'app/shared/model/user-review.model';

@Component({
  selector: 'jhi-user-review-detail',
  templateUrl: './user-review-detail.component.html'
})
export class UserReviewDetailComponent implements OnInit {
  userReview: IUserReview;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userReview }) => {
      this.userReview = userReview;
    });
  }

  previousState() {
    window.history.back();
  }
}
