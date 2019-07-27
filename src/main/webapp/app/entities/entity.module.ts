import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-job',
        loadChildren: './user-job/user-job.module#UneedUserJobModule'
      },
      {
        path: 'user-info',
        loadChildren: './user-info/user-info.module#UneedUserInfoModule'
      },
      {
        path: 'address',
        loadChildren: './address/address.module#UneedAddressModule'
      },
      {
        path: 'user-stat',
        loadChildren: './user-stat/user-stat.module#UneedUserStatModule'
      },
      {
        path: 'user-review',
        loadChildren: './user-review/user-review.module#UneedUserReviewModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UneedEntityModule {}
