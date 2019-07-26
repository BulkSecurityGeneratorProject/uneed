import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UneedSharedModule } from 'app/shared';
import {
  UserReviewComponent,
  UserReviewDetailComponent,
  UserReviewUpdateComponent,
  UserReviewDeletePopupComponent,
  UserReviewDeleteDialogComponent,
  userReviewRoute,
  userReviewPopupRoute
} from './';

const ENTITY_STATES = [...userReviewRoute, ...userReviewPopupRoute];

@NgModule({
  imports: [UneedSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserReviewComponent,
    UserReviewDetailComponent,
    UserReviewUpdateComponent,
    UserReviewDeleteDialogComponent,
    UserReviewDeletePopupComponent
  ],
  entryComponents: [UserReviewComponent, UserReviewUpdateComponent, UserReviewDeleteDialogComponent, UserReviewDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedUserReviewModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
