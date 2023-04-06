import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CertificateViewComponent } from './certificate-view/certificate-view.component';
import { CertificateModelComponent } from './certificate-model/certificate-model.component';



@NgModule({
  declarations: [
    CertificateViewComponent,
    CertificateModelComponent
  ],
  imports: [
    CommonModule,
  ],
  exports: [
    CertificateModelComponent,
    CertificateViewComponent
  ]
})
export class CertificateModule { }
