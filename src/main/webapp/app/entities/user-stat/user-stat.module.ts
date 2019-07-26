import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UneedSharedModule } from 'app/shared';
import {
  UserStatComponent,
  UserStatDetailComponent,
  UserStatUpdateComponent,
  UserStatDeletePopupComponent,
  UserStatDeleteDialogComponent,
  userStatRoute,
  userStatPopupRoute
} from './';

const ENTITY_STATES = [...userStatRoute, ...userStatPopupRoute];

@NgModule({
  imports: [UneedSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserStatComponent,
    UserStatDetailComponent,
    UserStatUpdateComponent,
    UserStatDeleteDialogComponent,
    UserStatDeletePopupComponent
  ],
  entryComponents: [UserStatComponent, UserStatUpdateComponent, UserStatDeleteDialogComponent, UserStatDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedUserStatModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
