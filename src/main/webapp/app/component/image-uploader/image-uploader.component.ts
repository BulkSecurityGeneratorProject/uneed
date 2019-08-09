import { Component, Input, OnInit, Output, ViewEncapsulation, EventEmitter } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-image-uploader',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './image-uploader.component.html',
  styleUrls: ['./image-uploader.component.scss']
})
export class ImageUploaderComponent implements OnInit {
  @Input() image: any;
  @Output() imageChange = new EventEmitter();

  file: File;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit() {
    console.log(this.image);
  }

  change(event) {
    if (event.target.files.length > 0) {
      this.file = event.target.files[0];
      this.preview();
    }
  }

  preview() {
    const mimeType = this.file.type;
    if (mimeType.match(/image\/*/) == null) {
      return;
    }
    const reader = new FileReader();
    reader.readAsDataURL(this.file);
    reader.onload = () => {
      this.image = reader.result;
      this.imageChange.emit(this.image);
    };
  }

  sanitizeImage(image): any {
    return this.sanitizer.bypassSecurityTrustUrl(atob(image));
  }
}
