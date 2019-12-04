import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginRequest } from '../model/LoginRequest';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  obtainAccessToken(login: LoginRequest) {
    const params = new URLSearchParams();
    params.append('email', login.email);
    params.append('password', login.password);
    params.append('grant_type','password');
    params.append('client_id','fooClientIdPassword');
    let headers =
      new Headers({'Content-type': 'application/json',
      'Authorization': 'Basic '+btoa("fooClientIdPassword:secret")});
    let options = new RequestOptions({ headers: headers });

    this._http.post('http://localhost:8081/spring-security-oauth-server/oauth/token',
      params.toString(), options)
      .map(res => res.json())
      .subscribe(
        data => this.saveToken(data),
        err => alert('Invalid Credentials'));
  }

  // saveToken(token){
  //   var expireDate = new Date().getTime() + (1000 * token.expires_in);
  //   Cookie.set("access_token", token.access_token, expireDate);
  //   this._router.navigate(['/']);
  // }

  // getResource(resourceUrl) : Observable<Foo>{
  //   var headers =
  //     new Headers({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
  //     'Authorization': 'Bearer '+Cookie.get('access_token')});
  //   var options = new RequestOptions({ headers: headers });
  //   return this._http.get(resourceUrl, options)
  //                  .map((res:Response) => res.json())
  //                  .catch((error:any) =>
  //                    Observable.throw(error.json().error || 'Server error'));
  // }

  // checkCredentials(){
  //   if (!Cookie.check('access_token')){
  //       this._router.navigate(['/login']);
  //   }
  // }

  // logout() {
  //   Cookie.delete('access_token');
  //   this._router.navigate(['/login']);
  // }
}
