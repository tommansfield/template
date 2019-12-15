import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Constants } from '../Constants';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {

  private token: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cookie: CookieService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.token = params['token'];
      this.convertAccessToken();
    });
  }

  convertAccessToken() {
    this.cookie.set('access_token', this.token, Constants.TOKENEXPIRATION, '/');
    this.router.navigate(['/home']);
  }

}
