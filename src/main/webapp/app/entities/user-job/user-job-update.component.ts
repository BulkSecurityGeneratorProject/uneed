import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IUserJob, UserJob } from 'app/shared/model/user-job.model';
import { UserJobService } from './user-job.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-user-job-update',
  templateUrl: './user-job-update.component.html'
})
export class UserJobUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    price: [],
    currency: [],
    imageUrl: [],
    createDate: [],
    lastUpdateDate: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userJobService: UserJobService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userJob }) => {
      this.updateForm(userJob);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userJob: IUserJob) {
    this.editForm.patchValue({
      id: userJob.id,
      name: userJob.name,
      description: userJob.description,
      price: userJob.price,
      currency: userJob.currency,
      imageUrl: userJob.imageUrl,
      createDate: userJob.createDate != null ? userJob.createDate.format(DATE_TIME_FORMAT) : null,
      lastUpdateDate: userJob.lastUpdateDate != null ? userJob.lastUpdateDate.format(DATE_TIME_FORMAT) : null,
      userId: userJob.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userJob = this.createFromForm();
    if (userJob.id !== undefined) {
      this.subscribeToSaveResponse(this.userJobService.update(userJob));
    } else {
      this.subscribeToSaveResponse(this.userJobService.create(userJob));
    }
  }

  private createFromForm(): IUserJob {
    return {
      ...new UserJob(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      price: this.editForm.get(['price']).value,
      currency: this.editForm.get(['currency']).value,
      imageUrl: this.editForm.get(['imageUrl']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      lastUpdateDate:
        this.editForm.get(['lastUpdateDate']).value != null
          ? moment(this.editForm.get(['lastUpdateDate']).value, DATE_TIME_FORMAT)
          : undefined,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserJob>>) {
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
