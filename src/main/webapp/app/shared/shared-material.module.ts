import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTooltipModule } from '@angular/material';

@NgModule({
  imports: [BrowserAnimationsModule, MatTooltipModule],
  exports: [BrowserAnimationsModule, MatTooltipModule]
})
export class UneedSharedMaterialModule {
  static forRoot() {
    return {
      ngModule: UneedSharedMaterialModule
    };
  }
}
