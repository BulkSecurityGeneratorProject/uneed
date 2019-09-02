import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UneedSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { HomeService } from 'app/home/home.service';
import { EntryComponent } from 'app/home/entry/entry.component';
import { SearchComponent } from 'app/home/search/search.component';
import { StarRatingModule } from 'app/component';

@NgModule({
  imports: [UneedSharedModule, StarRatingModule, RouterModule.forChild(HOME_ROUTE)],
  providers: [HomeService],
  declarations: [HomeComponent, EntryComponent, SearchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedHomeModule {}
