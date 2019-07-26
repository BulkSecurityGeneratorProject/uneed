import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IUserReview, UserReview } from 'app/shared/model/user-review.model';
import { UserReviewService } from './user-review.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-user-review-update',
  templateUrl: './user-review-update.component.html'
})
export class UserReviewUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    score: [null, [Validators.required, Validators.min(0), Validators.max(10)]],
    comment: [],
    date: [],
    reviewer: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userReviewService: UserReviewService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userReview }) => {
      this.updateForm(userReview);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userReview: IUserReview) {
    this.editForm.patchValue({
      id: userReview.id,
      score: userReview.score,
      comment: userReview.comment,
      date: userReview.date != null ? userReview.date.format(DATE_TIME_FORMAT) : null,
      reviewer: userReview.reviewer,
      user: userReview.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userReview = this.createFromForm();
    if (userReview.id !== undefined) {
      this.subscribeToSaveResponse(this.userReviewService.update(userReview));
    } else {
      this.subscribeToSaveResponse(this.userReviewService.create(userReview));
    }
  }

  private createFromForm(): IUserReview {
    return {
      ...new UserReview(),
      id: this.editForm.get(['id']).value,
      score: this.editForm.get(['score']).value,
      comment: this.editForm.get(['comment']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      reviewer: this.editForm.get(['reviewer']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserReview>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
