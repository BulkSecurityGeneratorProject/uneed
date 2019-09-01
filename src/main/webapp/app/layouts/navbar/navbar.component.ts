import { AfterContentInit, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';

import { AVATAR, VERSION } from 'app/app.constants';
import { JhiLanguageHelper, AccountService, LoginModalService, LoginService } from 'app/core';
import { ProfileService } from 'app/layouts/profiles/profile.service';

import { faCoffee, faUserCircle, faAddressCard, faChartLine } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit, AfterContentInit {
  inProduction: boolean;
  isNavbarCollapsed: boolean;
  languages: any[];
  swaggerEnabled: boolean;
  modalRef: NgbModalRef;
  version: string;
  lanKey: string;
  faCoffee = faCoffee;
  faAddressCard = faAddressCard;
  faChartLine = faChartLine;
  faUserCircle = faUserCircle;
  domNav: any;
  constructor(
    private loginService: LoginService,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private router: Router
  ) {
    this.version = VERSION ? 'v' + VERSION : '';
    this.isNavbarCollapsed = true;
  }

  ngOnInit() {
    this.lanKey = this.sessionStorage.retrieve('locale');
    this.languageHelper.getAll().then(languages => {
      this.languages = languages;
    });

    this.profileService.getProfileInfo().then(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });
  }

  changeLanguage(languageKey: string) {
    this.lanKey = languageKey;
    this.sessionStorage.store('locale', languageKey);
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  logout() {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
    this.isNavbarCollapsed ? this.removeTransparent() : this.addTransparent();
  }

  getImageUrl() {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : AVATAR;
  }

  ngAfterContentInit() {
    this.domNav = document.getElementById('nav');
    (() => {
      window.addEventListener('scroll', this.listener);
    })();
  }

  private listener = () => {
    window.scrollY > 1.5 ? this.addTransparent() : this.removeTransparent();
  };

  private addTransparent() {
    this.domNav.classList.remove('navbar-transparent');
  }

  private removeTransparent() {
    this.domNav.classList.add('navbar-transparent');
  }
}
