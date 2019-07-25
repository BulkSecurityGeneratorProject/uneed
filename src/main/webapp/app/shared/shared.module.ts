import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { UneedSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [UneedSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [UneedSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedSharedModule {
  static forRoot() {
    return {
      ngModule: UneedSharedModule
    };
  }
}
