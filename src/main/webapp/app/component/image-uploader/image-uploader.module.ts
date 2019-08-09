import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageUploaderComponent } from './image-uploader.component';
import { MatButtonModule } from '@angular/material';

@NgModule({
  declarations: [ImageUploaderComponent],
  exports: [ImageUploaderComponent],
  imports: [CommonModule, MatButtonModule]
})
export class ImageUploaderModule {}
