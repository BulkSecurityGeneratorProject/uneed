import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CookieModule } from 'ngx-cookie';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UneedSharedMaterialModule } from 'app/shared/shared-material.module';

@NgModule({
  imports: [NgbModule, InfiniteScrollModule, CookieModule.forRoot(), FontAwesomeModule, ReactiveFormsModule, UneedSharedMaterialModule],
  exports: [
    FormsModule,
    CommonModule,
    NgbModule,
    NgJhipsterModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    UneedSharedMaterialModule
  ]
})
export class UneedSharedLibsModule {
  static forRoot() {
    return {
      ngModule: UneedSharedLibsModule
    };
  }
}
