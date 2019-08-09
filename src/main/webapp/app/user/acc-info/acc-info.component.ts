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

  // image = "data:image/png;base64,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";
  image;
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

  updateImage(event) {
    console.log(this.image);
  }
}
