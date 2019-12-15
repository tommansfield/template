import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Constants } from '../Constants';
import { User } from '../entity/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  private baseUrl = `${Constants.APIURL}/user`;

  getUser(): Observable<User> {
    return this.http.get<User>(this.baseUrl + '/me');
  }

}
