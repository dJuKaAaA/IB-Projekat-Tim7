import { Component, ElementRef, Input, Output, Renderer2, ViewChild } from '@angular/core';
import { CertificateDemandResponse } from 'src/app/core/models/certificate-demand-response.model';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { CertificateDemandService } from 'src/app/core/services/certificate-demand.service';

@Component({
  selector: 'app-certificate-demand-history',
  templateUrl: './certificate-demand-history.component.html',
  styleUrls: ['./certificate-demand-history.component.css']
})
export class CertificateDemandHistoryComponent {
  certificateDemands:CertificateDemandResponse[];
  constructor(private certificateDemandService:CertificateDemandService, private authService:AuthService){}

  ngOnInit(): void {
    this.certificateDemandService.getByRequesterId(this.authService.getId(), 0, 100).subscribe(
      (data) => {
        this.certificateDemands = data.content
      }
    )
  }

}
