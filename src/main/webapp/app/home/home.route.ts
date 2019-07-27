import { Route } from '@angular/router';

import { HomeComponent } from './';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const HOME_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  resolve: {
    pagingParams: JhiResolvePagingParams
  },
  data: {
    authorities: [],
    pageTitle: 'home.title',
    defaultSort: 'id,asc'
  }
};
