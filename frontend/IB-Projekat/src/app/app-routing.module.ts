import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateAccountComponent } from './components/create-account/create-account.component';
import { LoginComponent } from './components/login/login.component';
import { CertificateViewComponent } from './components/certificate-view/certificate-view.component';
import { VerifyRegistrationComponent } from './components/verify-registration/verify-registration.component';
import { PasswordRecoveryStep1Component } from './components/passwordRecovery/password-recovery-step1/password-recovery-step1.component';
import { PasswordRecoveryStep2Component } from './components/passwordRecovery/password-recovery-step2/password-recovery-step2.component';
import { PasswordRecoveryStep3Component } from './components/passwordRecovery/password-recovery-step3/password-recovery-step3.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { MyCertificatesComponent } from './components/my-certificates/my-certificates.component';
import { CertificateDemandHistoryComponent } from './components/certificate-demand-history/certificate-demand-history.component';
import { CertificateDemandsViewComponent } from './components/certificate-demands-view/certificate-demands-view.component';
import { NotAuthorizedGuard } from './guards/not-authorized.guard';
import { AuthorizedGuard } from './guards/authorized.guard';
import { LoginVerificationCodeComponent } from './components/login-verification-code/login-verification-code.component';
import { OAuthLoginComponent } from './components/oauth-login/oauth-login.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'verify-login',
    component: LoginVerificationCodeComponent,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'create-account',
    component: CreateAccountComponent,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'certificate-view',
    component: CertificateViewComponent,
    canActivate: [AuthorizedGuard],
  },
  {
    path: 'verify-registration',
    component: VerifyRegistrationComponent,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'password-recovery-step1',
    component: PasswordRecoveryStep1Component,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'password-recovery-step2',
    component: PasswordRecoveryStep2Component,
    canActivate: [NotAuthorizedGuard],
  },
  {
    path: 'password-recovery-step3',
    component: PasswordRecoveryStep3Component,
    canActivate: [NotAuthorizedGuard],
  },

  {
    path: 'reset-password',
    component: ResetPasswordComponent,
  },

  {
    path: 'my-certificates',
    component: MyCertificatesComponent,
    canActivate: [AuthorizedGuard],
  },
  {
    path: 'demand-history',
    component: CertificateDemandHistoryComponent,
    canActivate: [AuthorizedGuard],
  },
  {
    path: 'certificate-demands',
    component: CertificateDemandsViewComponent,
    canActivate: [AuthorizedGuard],
  },
  {
    path: 'oauth-login',
    component: OAuthLoginComponent,
    canActivate: [NotAuthorizedGuard],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
