import { Component } from '@angular/core';
import { CertificateDemandResponse } from 'src/app/core/models/certificate-demand-response.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { CertificateDemandService } from 'src/app/core/services/certificate-demand.service';

@Component({
  selector: 'app-certificate-demands-view',
  templateUrl: './certificate-demands-view.component.html',
  styleUrls: ['./certificate-demands-view.component.css']
})
export class CertificateDemandsViewComponent {
  certificateDemands:CertificateDemandResponse[];
  showButtons:boolean = false;
  constructor(private certificateDemandService:CertificateDemandService, private authService:AuthService){}

  ngOnInit(): void {
    this.certificateDemandService.getAll(this.authService.getId(), 0, 100).subscribe(
      (data) => {
        this.showButtons = false;
        this.certificateDemands = data.content;
      }
    )
  }
}
