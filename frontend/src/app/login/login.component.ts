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
  private rememberMe: boolean;
  private errors: string[];

  constructor(
    private authService: AuthService, 
    private cookie: CookieService, 
    private router: Router) { }

  ngOnInit() {
    this.cookie.delete('access_token');
    this.loginRequest = new LoginRequest();
  }

  login(): void {
    this.errors = null;
    const timeout = this.rememberMe ? 99999999999 : 3600000;
    this.authService.login(this.loginRequest)
      .subscribe(token => {
        this.cookie.set('access_token', atob(token.accessToken), timeout);
        this.router.navigate(['/home']);
      }, error => {
        this.errors = error.error.errors == null ? [error.error.message] : error.error.errors;
        this.loginRequest.password = null;
      });
  }

}
