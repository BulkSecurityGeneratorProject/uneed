import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IUserInfo, UserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from './user-info.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-user-info-update',
  templateUrl: './user-info-update.component.html'
})
export class UserInfoUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];
  birthDateDp: any;

  editForm = this.fb.group({
    id: [],
    phone: [],
    mobilePhone: [],
    emailFlag: [],
    smsFlag: [],
    birthDate: [],
    gender: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userInfoService: UserInfoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userInfo }) => {
      this.updateForm(userInfo);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userInfo: IUserInfo) {
    this.editForm.patchValue({
      id: userInfo.id,
      phone: userInfo.phone,
      mobilePhone: userInfo.mobilePhone,
      emailFlag: userInfo.emailFlag,
      smsFlag: userInfo.smsFlag,
      birthDate: userInfo.birthDate,
      gender: userInfo.gender,
      user: userInfo.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userInfo = this.createFromForm();
    if (userInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.userInfoService.update(userInfo));
    } else {
      this.subscribeToSaveResponse(this.userInfoService.create(userInfo));
    }
  }

  private createFromForm(): IUserInfo {
    return {
      ...new UserInfo(),
      id: this.editForm.get(['id']).value,
      phone: this.editForm.get(['phone']).value,
      mobilePhone: this.editForm.get(['mobilePhone']).value,
      emailFlag: this.editForm.get(['emailFlag']).value,
      smsFlag: this.editForm.get(['smsFlag']).value,
      birthDate: this.editForm.get(['birthDate']).value,
      gender: this.editForm.get(['gender']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserInfo>>) {
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
