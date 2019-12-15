import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../service/auth.service';
import { LoginRequest } from '../dto/LoginRequest';
import { Constants } from '../Constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private loginRequest: LoginRequest;
  private rememberMe: boolean = true;
  private errors: string[];
  private facebookUrl = Constants.FACEBOOKURL;
  constructor(
    private authService: AuthService, 
    private cookie: CookieService, 
    private router: Router) { }

  ngOnInit() {
    this.loginRequest = new LoginRequest();
  }

  login(): void {
    this.errors = null;
    const timeout = this.rememberMe ? Constants.TOKENEXPIRATION : Constants.REMEMBERMEEXPIRATION;
    this.authService.login(this.loginRequest)
      .subscribe(token => {
        this.cookie.set('access_token', token.accessToken, timeout, '/');
        this.router.navigate(['/home']);
      }, error => {
        this.errors = error.error.errors == null ? [error.error.message] : error.error.errors;
        this.loginRequest.password = null;
      });
  }

  facebookLogin() {
    this.router.navigate(['/externalRedirect', { externalUrl: Constants.FACEBOOKURL }]);
  }
  
}
