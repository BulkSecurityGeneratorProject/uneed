import { Component, OnInit } from '@angular/core';
import { IUserJob, UserJob } from 'app/shared/model/user-job.model';
import { JhiAlertService } from 'ng-jhipster';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';
import { FormBuilder, Validators } from '@angular/forms';
import { CategoryService } from 'app/entities/category';
import { TagService } from 'app/entities/tag';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { DATE_TIME_FORMAT } from 'app/shared';
import { Observable } from 'rxjs';
import { AccJobService } from './acc-job.service';
import { User } from 'app/core';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';

@Component({
  selector: 'jhi-myservice',
  templateUrl: './acc-job.component.html',
  styleUrls: ['./acc-job.component.scss']
})
export class AccJobComponent implements OnInit {
  isSaving: boolean;
  error: string;
  success: string;
  user: User;
  categories: ICategory[];
  tags: ITag[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    price: [],
    currency: [],
    imageUrl: [],
    createDate: [],
    lastUpdateDate: [],
    categoryId: [null, [Validators.required]],
    tags: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected accJobService: AccJobService,
    protected categoryService: CategoryService,
    protected tagService: TagService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.route.data.subscribe(({ user }) => (this.user = user.body ? user.body : user));

    this.accJobService
      .current()
      .pipe(
        filter((res: HttpResponse<IUserJob[]>) => res.ok),
        map((res: HttpResponse<IUserJob[]>) => res.body),
        filter((res: IUserJob[]) => res.length > 0),
        map((res: IUserJob[]) => res[0])
      )
      .subscribe((res: IUserJob) => this.updateForm(res), (res: HttpErrorResponse) => this.onError(res.message));
    this.categoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => this.setCategory(res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tagService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITag[]>) => response.body)
      )
      .subscribe((res: ITag[]) => (this.tags = res), (res: HttpErrorResponse) => this.onError(res.message));
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
      userId: userJob.userId ? userJob.userId : this.user.id
    });
  }

  previousState() {
    // window.history.back();
  }

  save() {
    this.isSaving = true;
    const userJob = this.createFromForm();
    if (userJob.id) {
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
      userId: this.editForm.get(['userId']).value ? this.editForm.get(['userId']).value : this.user.id
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserJob>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.success = 'OK';
    this.error = null;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
    this.success = null;
    this.error = 'ERROR';
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

  private setCategory(list: ICategory[]) {
    this.categories = list;
    if (list.length > 0 && !this.editForm.get(['categoryId']).value) {
      this.editForm.get(['categoryId']).setValue(list[0].id);
    }
  }
}
