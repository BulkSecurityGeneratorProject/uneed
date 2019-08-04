import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { Trie, EMAIL_ALREADY_USED_TYPE, KEYCODE_PROTECTED, LOGIN_ALREADY_USED_TYPE, PATTERN_DOMAIN, PATTERN_NAME } from 'app/shared';

import { LoginModalService } from 'app/core';
import { Register } from './register.service';

const INVALID_EMAIL = 'Invalid email';
const INVALID_DOMAIN = 'Invalid domain';
const ERR_NOT_EMPTY = 'cannot be empty';
const ERR_MISSING_AT = 'missing "@"';
const ERR_MISSING_DOT = 'missing "."';
const ERR_MISSING_NAME = 'missing name';
const ERR_IS_TYPO = 'is it a typo?';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit, AfterViewInit {
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  success: boolean;
  modalRef: NgbModalRef;

  registerForm = this.fb.group({
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*$')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]]
  });

  email: string;
  credentials: any;
  errorEmail: string;
  validate = false;
  dict = new Trie(); // auto-complete emails dict. trie
  len = null; // input string length

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: Register,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.success = false;
  }

  ngAfterViewInit() {
    this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
  }

  register() {
    let registerAccount = {};
    const login = this.registerForm.get(['login']).value;
    const email = this.registerForm.get(['email']).value;
    const password = this.registerForm.get(['password']).value;
    if (password !== this.registerForm.get(['confirmPassword']).value) {
      this.doNotMatch = 'ERROR';
    } else {
      registerAccount = { ...registerAccount, login, email, password };
      this.doNotMatch = null;
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.languageService.getCurrent().then(langKey => {
        registerAccount = { ...registerAccount, langKey };
        this.registerService.save(registerAccount).subscribe(
          () => {
            this.success = true;
          },
          response => this.processError(response)
        );
      });
    }
  }

  openLogin() {
    this.modalRef = this.loginModalService.open();
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }

  handleChange(e) {
    console.log('input change...');
    this.email = e.target.value;
    this.validate = false;
    this.errorEmail = '';
  }

  handleSubmit(e) {
    console.log('submit...');
    e.preventDefault();
    // this.email = e.target.value; // for test
    if (this.validation() && this.deliverable()) {
      this.errorEmail = '';
      this.validate = true;
    }
  }

  handleKeyPress(e) {
    const keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
    console.log('key press... ' + keyCode);
    if (KEYCODE_PROTECTED.indexOf(keyCode) >= 0) {
      return;
    }
    const target = e.target;
    this.len = target.value.length;
    let start = target.selectionStart;
    const end = target.selectionEnd;
    const atPos = target.value.indexOf('@');
    if (atPos < 0 && keyCode !== 64) {
      return;
    } // @=64
    let prefix = target.value.substring(atPos + 1, start) + String.fromCharCode(keyCode);
    if (keyCode === 64) {
      prefix = '';
    }
    e.preventDefault();
    // keycode for end
    if (/^(13|44|59)$/.test('' + keyCode)) {
      e.target.selectionStart = end + (target.value.length - this.len);
      e.target.selectionEnd = end + (target.value.length - this.len);
      if (this.validation() && this.deliverable()) {
        this.errorEmail = '';
        this.validate = true;
      }
      return;
    }
    // replace selection with input
    e.target.value = target.value.substr(0, start) + String.fromCharCode(keyCode) + target.value.substr(end, target.value.length);
    this.len = target.value.length - this.len;
    // move selection
    e.target.selectionStart = ++start;
    e.target.selectionEnd = end + this.len;
    // matched domain
    let words = [];
    if (keyCode === 64) {
      words = this.dict.getWords();
    } else {
      words = this.dict.getWordsByPrefix(prefix);
    }
    // auto complete
    if (words.length > 0) {
      const subStr = words[0].substr(prefix.length, words[0].length);
      e.target.value = target.value.substr(0, start) + subStr + target.value.substr(start, target.value.length);
      // highlight
      e.target.selectionStart = start;
      e.target.selectionEnd = start + subStr.length;
    }
    this.email = e.target.value;
  }

  onFocusOut(e) {
    if (this.validation() && this.deliverable()) {
      this.errorEmail = '';
      this.validate = true;
    }
  }

  validation() {
    const email = this.email;
    console.log('validate: ' + email);
    if (!email) {
      return this.setError(ERR_NOT_EMPTY);
    }
    if (typeof email !== undefined) {
      const lastAtPos = email.lastIndexOf('@');
      const lastDotPos = email.lastIndexOf('.');
      if (lastAtPos < 0) {
        return this.setError(ERR_MISSING_AT);
      }
      const domainPart = email.substr(lastAtPos + 1);
      const domainRegExp = new RegExp(PATTERN_DOMAIN);
      if (!domainRegExp.test(domainPart)) {
        return this.setError(INVALID_DOMAIN);
      }
      // if (lastDotPos < lastAtPos) {
      //   return this.setError(ERR_MISSING_DOT);
      // }
      const namePart = email.substr(0, lastAtPos);
      const nameRegExp = new RegExp(PATTERN_NAME);
      if (namePart.length === 0) {
        return this.setError(ERR_MISSING_NAME);
      }
      if (!nameRegExp.test(namePart)) {
        return this.setError(ERR_IS_TYPO);
      }
    }
    this.setEmail();
    return true;
  }

  private setEmail() {
    this.registerForm.patchValue({ email: this.email });
  }

  deliverable() {
    console.log('deliverable: ' + this.email);
    // let url = EMAIL_VERIFY_API_URL + '?email=' + encodeURI(this.email) + '&apikey=' + EMAIL_VERIFY_API_KEY;
    // using mode: "no-cors" will get an opaque response, which doesn't seem to return data in the body.
    // fetch(url, {method: 'GET', mode: 'no-cors', headers: {Accept: 'application/json'}})
    //     .then(res => {
    //         console.log(res);
    //         if (res.status === 200) return res.json();
    //     })
    //     .then(json => {
    //         /**do something**/
    //         console.log('You are using Kickbox\'s sandbox API: all email => 'result':'deliverable'');
    //     })
    //     .catch(error => console.error('Error:', error));
    return true;
  }

  setError(msg) {
    console.log(msg);
    this.errorEmail = INVALID_EMAIL + ': ' + msg;
    return false;
  }
}
