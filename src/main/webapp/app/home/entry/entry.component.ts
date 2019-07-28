import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IUserJob } from 'app/shared/model/user-job.model';
import { ITEMS_PER_PAGE } from 'app/shared';
import { HomeService } from 'app/home/home.service';

@Component({
  selector: 'jhi-entry',
  templateUrl: './entry.component.html'
})
export class EntryComponent implements OnInit, OnDestroy {
  @Input() criteria;
  userJobs: IUserJob[];
  error: any;
  success: any;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    protected homeService: HomeService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  ngOnInit() {
    this.criteria.subscribe(entry => this.query(entry));
  }

  query(entry) {
    const expected = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      'name.contains': entry,
      'description.contains': entry
    };
    this.homeService
      .query(expected)
      .subscribe(
        (res: HttpResponse<IUserJob[]>) => this.paginateUserJobs(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadAll() {
    this.homeService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IUserJob[]>) => this.paginateUserJobs(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/user-job'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/user-job',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnDestroy() {}

  trackId(index: number, item: IUserJob) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUserJobs(data: IUserJob[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.userJobs = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
