import { NgModule } from '@angular/core';
import { DashboardComponent } from './dashboard.component';
import { UneedSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { DashboardRoute } from 'app/dashboard/dashboard.route';

@NgModule({
  declarations: [DashboardComponent],
  imports: [UneedSharedModule, RouterModule.forChild(DashboardRoute)]
})
export class DashboardModule {}
