import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule, MatButtonModule, MatCheckboxModule, MatIconModule, MatToolbarModule } from '@angular/material';

@NgModule({
  imports: [BrowserAnimationsModule, MatInputModule, MatButtonModule, MatCheckboxModule, MatIconModule, MatToolbarModule],
  exports: [BrowserAnimationsModule, MatInputModule, MatButtonModule, MatCheckboxModule, MatIconModule, MatToolbarModule]
})
export class UneedSharedMaterialModule {
  static forRoot() {
    return {
      ngModule: UneedSharedMaterialModule
    };
  }
}
