import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { CreateAccountComponent } from './components/create-account/create-account.component';
import { CertificateViewComponent } from './components/certificate-view/certificate-view.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CertificateModelComponent } from './components/certificate-model/certificate-model.component';
import { NavbarComponent } from './components/navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CreateAccountComponent,
    CertificateViewComponent,
    CertificateModelComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
