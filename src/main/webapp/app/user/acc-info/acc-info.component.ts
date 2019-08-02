import { Component, OnInit } from '@angular/core';
import { IUserInfo, UserInfo } from 'app/shared/model/user-info.model';
import { JhiAlertService } from 'ng-jhipster';
import { FormBuilder, Validators } from '@angular/forms';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccInfoService } from './acc-info.service';
import { ActivatedRoute } from '@angular/router';
import { User } from 'app/core';

@Component({
  selector: 'jhi-myaccount',
  templateUrl: './acc-info.component.html',
  styleUrls: ['./acc-info.component.scss']
})
export class AccInfoComponent implements OnInit {
  isSaving: boolean;

  birthDateDp: any;
  user: User;
  editForm = this.fb.group({
    id: [],
    phone: [],
    mobilePhone: [],
    emailFlag: [],
    smsFlag: [],
    birthDate: [],
    gender: ['MALE', [Validators.required]],
    user: [Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected accInfoService: AccInfoService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.route.data.subscribe(({ user }) => (this.user = user.body ? user.body : user));

    this.accInfoService
      .current()
      .pipe(
        filter((res: HttpResponse<IUserInfo[]>) => res.ok),
        map((res: HttpResponse<IUserInfo[]>) => res.body),
        filter((res: IUserInfo[]) => res.length > 0),
        map((res: IUserInfo[]) => res[0])
      )
      .subscribe((res: IUserInfo) => this.updateForm(res), (res: HttpErrorResponse) => this.onError(res.message));
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
      user: userInfo.user ? userInfo.user : this.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userInfo = this.createFromForm();
    if (userInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.accInfoService.update(userInfo));
    } else {
      this.subscribeToSaveResponse(this.accInfoService.create(userInfo));
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
}
