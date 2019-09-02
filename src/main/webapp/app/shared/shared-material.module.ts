import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatTooltipModule, MatIconModule, MatSnackBarModule } from '@angular/material';

@NgModule({
  imports: [BrowserAnimationsModule, MatButtonModule, MatTooltipModule, MatIconModule, MatSnackBarModule],
  exports: [BrowserAnimationsModule, MatButtonModule, MatTooltipModule, MatIconModule, MatSnackBarModule]
})
export class UneedSharedMaterialModule {
  static forRoot() {
    return {
      ngModule: UneedSharedMaterialModule
    };
  }
}
