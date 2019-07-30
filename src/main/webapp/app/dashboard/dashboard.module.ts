import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { DashboardComponent } from './dashboard.component';
import { UneedSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { DashboardRoute } from './dashboard.route';
import { DashboardService } from './dashboard.service';

@NgModule({
  declarations: [DashboardComponent],
  providers: [DashboardService],
  imports: [UneedSharedModule, RouterModule.forChild(DashboardRoute)],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DashboardModule {}
