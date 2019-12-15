import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { UserService } from '../service/user.service';
import { Messages } from '../Messages';
import { User } from '../entity/User';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private user: User;

  constructor(
    private router: Router, 
    private cookie: CookieService,
    private userService: UserService) { }

  ngOnInit() {
    this.getUser();
  }

  getUser(): void {
    this.userService.getUser()
      .subscribe((user: User) => {
        this.user = user
      }, _ => {
          console.error(Messages.NOUSERACCOUNT);
          this.router.navigate(['/login']);
        } );
  }

  logout(): void {
    this.cookie.delete('access_token', '/');
    this.router.navigate(['/login']);
  }

}
