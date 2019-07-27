import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UneedSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { HomeService } from 'app/home/home.service';
import { EntryComponent } from 'app/home/component/entry.component';

@NgModule({
  imports: [UneedSharedModule, RouterModule.forChild([HOME_ROUTE])],
  providers: [HomeService],
  declarations: [HomeComponent, EntryComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedHomeModule {}
