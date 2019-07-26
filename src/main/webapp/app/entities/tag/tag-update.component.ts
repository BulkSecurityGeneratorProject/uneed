import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { IUserJob } from 'app/shared/model/user-job.model';
import { UserJobService } from 'app/entities/user-job';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html'
})
export class TagUpdateComponent implements OnInit {
  isSaving: boolean;

  userjobs: IUserJob[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    userJob: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tagService: TagService,
    protected userJobService: UserJobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);
    });
    this.userJobService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserJob[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserJob[]>) => response.body)
      )
      .subscribe((res: IUserJob[]) => (this.userjobs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(tag: ITag) {
    this.editForm.patchValue({
      id: tag.id,
      name: tag.name,
      userJob: tag.userJob
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  private createFromForm(): ITag {
    return {
      ...new Tag(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      userJob: this.editForm.get(['userJob']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>) {
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

  trackUserJobById(index: number, item: IUserJob) {
    return item.id;
  }
}
