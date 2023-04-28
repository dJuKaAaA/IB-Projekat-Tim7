import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateAccountComponent } from './components/create-account/create-account.component';
import { LoginComponent } from './components/login/login.component';
import { CertificateViewComponent } from './components/certificate-view/certificate-view.component';
import { VerifyRegistrationComponent } from './components/verify-registration/verify-registration.component';

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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
