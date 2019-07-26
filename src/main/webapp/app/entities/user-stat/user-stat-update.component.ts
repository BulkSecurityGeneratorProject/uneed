import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUserStat, UserStat } from 'app/shared/model/user-stat.model';
import { UserStatService } from './user-stat.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-user-stat-update',
  templateUrl: './user-stat-update.component.html'
})
export class UserStatUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    viewCount: [],
    reviewCount: [],
    rating: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userStatService: UserStatService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userStat }) => {
      this.updateForm(userStat);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userStat: IUserStat) {
    this.editForm.patchValue({
      id: userStat.id,
      viewCount: userStat.viewCount,
      reviewCount: userStat.reviewCount,
      rating: userStat.rating,
      user: userStat.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userStat = this.createFromForm();
    if (userStat.id !== undefined) {
      this.subscribeToSaveResponse(this.userStatService.update(userStat));
    } else {
      this.subscribeToSaveResponse(this.userStatService.create(userStat));
    }
  }

  private createFromForm(): IUserStat {
    return {
      ...new UserStat(),
      id: this.editForm.get(['id']).value,
      viewCount: this.editForm.get(['viewCount']).value,
      reviewCount: this.editForm.get(['reviewCount']).value,
      rating: this.editForm.get(['rating']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserStat>>) {
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
