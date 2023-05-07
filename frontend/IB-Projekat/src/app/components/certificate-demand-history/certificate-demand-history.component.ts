import { Component, ElementRef, Input, Output, Renderer2, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  showButtons:boolean = false;
  constructor(private certificateDemandService:CertificateDemandService, private authService:AuthService, private route:ActivatedRoute){}

  ngOnInit(): void {
    this.route.queryParams
      .subscribe(params => {
        if(params['value'] === "history"){
          this.certificateDemandService.getByRequesterId(this.authService.getId(), 0, 100).subscribe(
            (data) => {
              this.showButtons = false;
              this.certificateDemands = data.content;
            }
          )
        }
        else{
          this.certificateDemandService.getByRequesterIdPending(this.authService.getId(), 0, 100).subscribe(
            data => {
              this.showButtons = true;
              this.certificateDemands = data.content;
            }
          )
        }
      }
    );
  }

  removeCertificate(id:number){
    let counter = 0;
    if (this.certificateDemands != undefined)
      for(let certificate of this.certificateDemands){
        if(certificate.id == id){
          this.certificateDemands = this.certificateDemands.slice(counter - 1, counter)
          console.log(this.certificateDemands)
        }
        counter += 1
      }
  }

}
