import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Constants } from '../Constants';
import { LoginRequest } from '../dto/LoginRequest';
import { TokenResponse } from '../dto/TokenResponse';
import { SignUpRequest } from '../dto/SignUpRequest';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) {}

  private baseUrl = `${Constants.BASEURL}/auth`;

  login(login: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.baseUrl + '/login', login);
  }

  signUp(signUp: SignUpRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.baseUrl + '/signup', signUp);
  }

}
