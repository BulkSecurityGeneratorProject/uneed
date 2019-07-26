import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UneedSharedModule } from 'app/shared';
import {
  UserJobComponent,
  UserJobDetailComponent,
  UserJobUpdateComponent,
  UserJobDeletePopupComponent,
  UserJobDeleteDialogComponent,
  userJobRoute,
  userJobPopupRoute
} from './';

const ENTITY_STATES = [...userJobRoute, ...userJobPopupRoute];

@NgModule({
  imports: [UneedSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserJobComponent,
    UserJobDetailComponent,
    UserJobUpdateComponent,
    UserJobDeleteDialogComponent,
    UserJobDeletePopupComponent
  ],
  entryComponents: [UserJobComponent, UserJobUpdateComponent, UserJobDeleteDialogComponent, UserJobDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedUserJobModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
