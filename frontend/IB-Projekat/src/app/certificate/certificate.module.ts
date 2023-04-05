import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CertificateViewComponent } from './component/certificate-view/certificate-view.component';



@NgModule({
  declarations: [
    CertificateViewComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    CertificateViewComponent
  ]
})
export class CertificateModule { }
