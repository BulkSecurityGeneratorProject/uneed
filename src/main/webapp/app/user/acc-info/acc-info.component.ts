import { Component, OnInit } from '@angular/core';
import { IUserInfo, UserInfo } from 'app/shared/model/user-info.model';
import { JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { FormBuilder, Validators } from '@angular/forms';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccInfoService } from './acc-info.service';
import { ActivatedRoute } from '@angular/router';
import { AccountService, JhiLanguageHelper, User } from 'app/core';

@Component({
  selector: 'jhi-myaccount',
  templateUrl: './acc-info.component.html',
  styleUrls: ['./acc-info.component.scss']
})
export class AccInfoComponent implements OnInit {
  isSaving: boolean;
  error: string;
  success: string;
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
    user: []
  });

  languages: any[];
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [false],
    authorities: [[]],
    langKey: ['en'],
    login: [],
    imageUrl: []
  });

  constructor(
    private accountService: AccountService,
    private accInfoService: AccInfoService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private jhiAlertService: JhiAlertService,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.route.data.subscribe(({ user }) => (this.user = user.body ? user.body : user));
    this.accountService.identity().then(account => {
      this.updateAccount(account);
    });
    this.languageHelper.getAll().then(languages => {
      this.languages = languages;
    });
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
    // window.history.back();
  }

  save() {
    this.saveAccount();
    this.isSaving = true;
    const userInfo = this.createFromForm();
    if (userInfo.id) {
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
      user: this.editForm.get(['user']).value ? this.editForm.get(['user']).value : this.user
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

  saveAccount() {
    const settingsAccount = this.accountFromForm();
    this.accountService.save(settingsAccount).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
        this.accountService.identity(true).then(account => {
          this.updateAccount(account);
        });
        this.languageService.getCurrent().then(current => {
          if (settingsAccount.langKey !== current) {
            this.languageService.changeLanguage(settingsAccount.langKey);
          }
        });
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  private accountFromForm(): any {
    const account = {};
    return {
      ...account,
      firstName: this.settingsForm.get('firstName').value,
      lastName: this.settingsForm.get('lastName').value,
      email: this.settingsForm.get('email').value,
      activated: this.settingsForm.get('activated').value,
      authorities: this.settingsForm.get('authorities').value,
      langKey: this.settingsForm.get('langKey').value,
      login: this.settingsForm.get('login').value,
      imageUrl: this.settingsForm.get('imageUrl').value
    };
  }

  updateAccount(account: any): void {
    this.settingsForm.patchValue({
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email,
      activated: account.activated,
      authorities: account.authorities,
      langKey: account.langKey,
      login: account.login,
      imageUrl: account.imageUrl
    });
  }
}
