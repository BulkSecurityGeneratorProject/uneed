import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html'
})
export class SearchComponent implements OnInit, OnDestroy {
  @Output() criteria: any = new EventEmitter();

  content: any = new FormControl('');

  constructor() {}

  ngOnInit() {}

  search() {
    this.criteria.next(this.content.value);
  }

  ngOnDestroy() {}
}
