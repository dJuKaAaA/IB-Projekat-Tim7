import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
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
import { JwtInterceptor } from './interceptor/jwt.interceptor';
import { DemandCreationDialogComponent } from './components/demand-creation-dialog/demand-creation-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { VerifyRegistrationComponent } from './components/verify-registration/verify-registration.component';
import { SendVerificationCodeComponent } from './components/send-verification-code/send-verification-code.component';
import { PasswordRecoveryStep1Component } from './components/passwordRecovery/password-recovery-step1/password-recovery-step1.component';
import { PasswordRecoveryStep2Component } from './components/passwordRecovery/password-recovery-step2/password-recovery-step2.component';
import { PasswordRecoveryStep3Component } from './components/passwordRecovery/password-recovery-step3/password-recovery-step3.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { ValidateCertificateDialogComponent } from './components/validate-certificate-dialog/validate-certificate-dialog.component';
import { ValidateFromUploadComponent } from './components/validate-from-upload/validate-from-upload.component';
import { MyCertificatesComponent } from './components/my-certificates/my-certificates.component';
import { CertificateDemandHistoryComponent } from './components/certificate-demand-history/certificate-demand-history.component';
import { CertificateDemandModelComponent } from './components/certificate-demand-model/certificate-demand-model.component';
import { CertificateReviewComponent } from './components/certificate-review/certificate-review.component';
import { DownloadCertificateComponent } from './components/download-certificate/download-certificate.component';
import { CertificateDemandsViewComponent } from './components/certificate-demands-view/certificate-demands-view.component';
import { RetractCertificateDialogComponent } from './components/retract-certificate-dialog/retract-certificate-dialog.component';
import { NgxCaptchaModule } from 'ngx-captcha';
import { LoginVerificationCodeComponent } from './components/login-verification-code/login-verification-code.component';
import { OAuthLoginComponent } from './components/oauth-login/oauth-login.component';
import { SessionExpiredCheckInterceptor } from './interceptor/session-expired-check.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CreateAccountComponent,
    CertificateViewComponent,
    CertificateModelComponent,
    NavbarComponent,
    DemandCreationDialogComponent,
    VerifyRegistrationComponent,
    SendVerificationCodeComponent,
    PasswordRecoveryStep1Component,
    PasswordRecoveryStep2Component,
    PasswordRecoveryStep3Component,
    ResetPasswordComponent,
    ValidateCertificateDialogComponent,
    ValidateFromUploadComponent,
    MyCertificatesComponent,
    CertificateDemandHistoryComponent,
    CertificateDemandModelComponent,
    CertificateReviewComponent,
    DownloadCertificateComponent,
    CertificateDemandsViewComponent,
    RetractCertificateDialogComponent,
    LoginVerificationCodeComponent,
    OAuthLoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    NgxCaptchaModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: SessionExpiredCheckInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
