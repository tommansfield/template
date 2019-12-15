import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { ActivatedRouteSnapshot, RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { InjectionToken } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CookieService } from 'ngx-cookie-service';
import { CustomHttpInterceptor } from './CustomHttpInterceptor';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HomeComponent } from './home/home.component';
import { CallbackComponent } from './callback/callback.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ExternalUrlDirective } from './external-url.directive';
import { Constants } from './Constants';

const externalUrlProvider = new InjectionToken('externalUrlRedirectResolver');

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: `${Constants.CALLBACKURL}/:token`, component: CallbackComponent },
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'externalRedirect', canActivate: [externalUrlProvider], component: NotFoundComponent }

];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    HomeComponent,
    CallbackComponent,
    NotFoundComponent,
    ExternalUrlDirective
  ],
  imports: [
    BrowserModule,
    RouterModule,
    FormsModule,
    RouterModule.forRoot(routes),
    HttpClientModule,
    BrowserAnimationsModule
  ],
  exports: [RouterModule],
  providers: [
    CookieService,
    { provide: HTTP_INTERCEPTORS,
      useClass: CustomHttpInterceptor,
      multi: true
   },
   {
    provide: externalUrlProvider,
    useValue: (route: ActivatedRouteSnapshot) => {
        const externalUrl = route.paramMap.get('externalUrl');
        window.open(externalUrl, '_self');
    },
},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
