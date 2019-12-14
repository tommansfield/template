import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
// import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../service/auth.service';
import { LoginRequest } from '../model/LoginRequest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginRequest: LoginRequest;
  public rememberMe: boolean;
  public errors: string[];
// private cookie: CookieService, 
  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.loginRequest = new LoginRequest();
  }

  login(): void {
    this.errors = null;
    const timeout = this.rememberMe ? 99999999999 : 3600000;
    this.authService.login(this.loginRequest)
      .subscribe(token => {
        console.log("not error ?");
    //    this.cookie.set('access_token', atob(token.accessToken), timeout);
        this.router.navigate(['/home']);
      }, error => {
        this.errors = error.error.errors;
        this.loginRequest.password = null;
      });
  }

}
