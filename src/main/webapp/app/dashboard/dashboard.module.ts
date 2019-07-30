import { NgModule } from '@angular/core';
import { DashboardComponent } from './dashboard.component';
import { UneedSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { DashboardRoute } from './dashboard.route';
import { DashboardService } from './dashboard.service';

@NgModule({
  declarations: [DashboardComponent],
  providers: [DashboardService],
  imports: [UneedSharedModule, RouterModule.forChild(DashboardRoute)]
})
export class DashboardModule {}
