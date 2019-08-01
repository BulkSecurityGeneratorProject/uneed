import { Component, OnInit } from '@angular/core';
import { IUserJob, UserJob } from 'app/shared/model/user-job.model';
import { JhiAlertService } from 'ng-jhipster';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';
import { IUser, UserService } from 'app/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CategoryService } from 'app/entities/category';
import { TagService } from 'app/entities/tag';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { DATE_TIME_FORMAT } from 'app/shared';
import { Observable } from 'rxjs';
import { AccJobService } from './acc-job.service';
import * as moment from 'moment';

@Component({
  selector: 'jhi-myservice',
  templateUrl: './acc-job.component.html',
  styleUrls: ['./acc-job.component.scss']
})
export class AccJobComponent implements OnInit {
  isSaving: boolean;

  categories: ICategory[];

  tags: ITag[];

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
    categoryId: [],
    tags: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected accJobService: AccJobService,
    protected categoryService: CategoryService,
    protected tagService: TagService,
    protected userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.accJobService
      .current()
      .pipe(
        filter((res: HttpResponse<IUserJob[]>) => res.ok),
        map((res: HttpResponse<IUserJob[]>) => res.body)
      )
      .subscribe(
        (res: IUserJob[]) => {
          if (res && res.length > 0) {
            this.updateForm(res[0]);
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.categoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tagService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITag[]>) => response.body)
      )
      .subscribe((res: ITag[]) => (this.tags = res), (res: HttpErrorResponse) => this.onError(res.message));
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
      categoryId: userJob.categoryId,
      tags: userJob.tags,
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
      this.subscribeToSaveResponse(this.accJobService.update(userJob));
    } else {
      this.subscribeToSaveResponse(this.accJobService.create(userJob));
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
      categoryId: this.editForm.get(['categoryId']).value,
      tags: this.editForm.get(['tags']).value,
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

  trackCategoryById(index: number, item: ICategory) {
    return item.id;
  }

  trackTagById(index: number, item: ITag) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
