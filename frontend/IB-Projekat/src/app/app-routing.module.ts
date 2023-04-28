import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateAccountComponent } from './components/create-account/create-account.component';
import { LoginComponent } from './components/login/login.component';
import { CertificateViewComponent } from './components/certificate-view/certificate-view.component';
import { VerifyRegistrationComponent } from './components/verify-registration/verify-registration.component';
import { PasswordRecoveryStep1Component } from './components/passwordRecovery/password-recovery-step1/password-recovery-step1.component';
import { PasswordRecoveryStep2Component } from './components/passwordRecovery/password-recovery-step2/password-recovery-step2.component';
import { PasswordRecoveryStep3Component } from './components/passwordRecovery/password-recovery-step3/password-recovery-step3.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'create-account',
    component: CreateAccountComponent,
  },
  {
    path: 'certificate-view',
    component: CertificateViewComponent,
  },
  {
    path: 'verify-registration',
    component: VerifyRegistrationComponent,
  },
  {
    path: 'password-recovery-step1',
    component: PasswordRecoveryStep1Component,
  },
  {
    path: 'password-recovery-step2',
    component: PasswordRecoveryStep2Component,
  },
  {
    path: 'password-recovery-step3',
    component: PasswordRecoveryStep3Component,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
