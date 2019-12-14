import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { Constants } from '../Constants';
import { LoginRequest } from '../model/LoginRequest';
import { TokenResponse } from '../model/TokenResponse';
import { SignUpRequest } from '../model/SignUpRequest';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  private baseUrl = `${Constants.BASEURL}/auth`;

  login(login: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.baseUrl + '/login', login);
  }

  signUp(signUp: SignUpRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.baseUrl + '/signup', signUp)
    .pipe(
      tap((token: TokenResponse) => console.log('Created new user account')),
      catchError(this.handleError<TokenResponse>('signup')));
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

}
