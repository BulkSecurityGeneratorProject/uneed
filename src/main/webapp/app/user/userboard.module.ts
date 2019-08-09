import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UneedSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { RouterModule } from '@angular/router';
import { UserboardRoute } from './userboard.route';
import { UserboardService } from './userboard.service';
import { AccConfigComponent } from './acc-config/acc-config.component';
import { AccInfoComponent } from './acc-info/acc-info.component';
import { AccJobComponent } from './acc-job/acc-job.component';
import { AccInfoService } from './acc-info/acc-info.service';
import { AccJobService } from 'app/user/acc-job/acc-job.service';
import { ImageUploaderModule } from 'app/component';

@NgModule({
  declarations: [DashboardComponent, AccConfigComponent, AccInfoComponent, AccJobComponent],
  providers: [UserboardService, AccInfoService, AccJobService, { provide: JhiLanguageService, useClass: JhiLanguageService }],
  imports: [UneedSharedModule, ImageUploaderModule, RouterModule.forChild(UserboardRoute)],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UserboardModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
