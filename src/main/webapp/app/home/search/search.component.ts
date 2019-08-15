import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { CATEGORIES } from 'app/shared/constants/model.constants';
import {
  faCoffee,
  faMapMarked,
  faUsers,
  faUtensils,
  faCity,
  faCarSide,
  faLaptop,
  faUserTie,
  faDumbbell,
  faPlaneDeparture
} from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.scss']
})
export class SearchComponent implements OnInit, OnDestroy {
  @Output() criteria: any = new EventEmitter();

  faCoffee = faCoffee;
  faMapMarked = faMapMarked;
  faUsers = faUsers;
  faUtensils = faUtensils;
  faCity = faCity;
  faCarSide = faCarSide;
  faLaptop = faLaptop;
  faUserTie = faUserTie;
  faDumbbell = faDumbbell;
  faPlaneDeparture = faPlaneDeparture;

  form = this.fb.group({
    type: ['service'],
    content: [''],
    categoryId: [null],
    location: [null, [Validators.required]]
  });

  categories: ICategory[];
  tags: ITag[];
  hasCategory = true;

  constructor(protected categoryService: CategoryService, protected tagService: TagService, private fb: FormBuilder) {}

  ngOnInit() {
    this.setCategory(CATEGORIES);
    // this.categoryService
    //   .query()
    //   .pipe(
    //     filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
    //     map((response: HttpResponse<ICategory[]>) => response.body)
    //   )
    //   .subscribe((res: ICategory[]) => this.setCategory(res), (res: HttpErrorResponse) => this.onError(res.message));
    //
    // this.tagService
    //   .query()
    //   .pipe(
    //     filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
    //     map((response: HttpResponse<ITag[]>) => response.body)
    //   )
    //   .subscribe((res: ITag[]) => (this.tags = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  search() {
    this.criteria.next(this.form.get(['content']).value);
  }

  private setCategory(list: ICategory[]) {
    this.categories = list;
    // if (list.length > 0) {
    //   this.form.get(['categoryId']).setValue(list[0].id);
    // }
  }

  protected onError(errorMessage: string) {
    console.error(errorMessage);
  }

  trackCategoryById(index: number, item: ICategory) {
    return item.id;
  }

  trackTagById(index: number, item: ITag) {
    return item.id;
  }

  onTypeChange(type: string) {
    this.form.get(['type']).setValue(type);
    switch (type) {
      case 'place':
      case 'restaurant':
        this.hasCategory = false;
        break;
      case 'group':
      case 'service':
      default:
        this.hasCategory = true;
        break;
    }
  }

  ngOnDestroy() {}
}
