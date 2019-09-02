import { NgModule } from '@angular/core';
import { StarRatingComponent } from './star-rating.component';
import { UneedSharedModule } from 'app/shared';

@NgModule({
  declarations: [StarRatingComponent],
  exports: [StarRatingComponent],
  imports: [UneedSharedModule]
})
export class StarRatingModule {}
