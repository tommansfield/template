import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../../service/auth.service';
import { LoginRequest } from '../../dto/LoginRequest';
import { Constants } from '../../Constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  public loginRequest: LoginRequest = new LoginRequest();
  public rememberMe: boolean = true;
  public errors: string[];
 
  constructor(
    private auth: AuthService, 
    private cookie: CookieService, 
    private router: Router) {}

  login(): void {
    this.errors = null;
    const timeout = this.rememberMe ? Constants.REMEMBERMEEXPIRATION : Constants.TOKENEXPIRATION;
    this.auth.login(this.loginRequest)
      .subscribe(token => {
        this.cookie.set('access_token', token.accessToken, timeout, '/');
        this.router.navigate(['/home']);
      }, error => {
        this.errors = error.error.errors == null ? [error.error.message] : error.error.errors;
        this.loginRequest.password = null;
      });
  }

  getLoginPage(provider: string): string {
    switch (provider) {
      case 'facebook': return Constants.FACEBOOKURL;
      case 'google': return Constants.GOOGLEURL;
      case 'github': return Constants.GITHUBURL;
    }
  }
  
}
