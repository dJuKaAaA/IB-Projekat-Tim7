import { Component, EventEmitter, Input, Output, Renderer2 } from '@angular/core';
import { CertificateDemandResponse } from 'src/app/core/models/certificate-demand-response.model';
import { CertificateDemandService } from 'src/app/core/services/certificate-demand.service';

@Component({
  selector: 'app-certificate-demand-model',
  templateUrl: './certificate-demand-model.component.html',
  styleUrls: ['./certificate-demand-model.component.css']
})
export class CertificateDemandModelComponent {
  @Input() certificate: CertificateDemandResponse = {} as CertificateDemandResponse;
  @Input() showButtons: boolean;
  @Output() changedStatus = new EventEmitter();
  
  

  constructor(
    private renderer: Renderer2, private certificateDemandService:CertificateDemandService
  ) {}


  allDataVisible: boolean = false;
  showDataLabel: string = "Show more..."

  changeDataVisibility() {
    this.allDataVisible = !this.allDataVisible;
    if (this.allDataVisible) {
      this.showDataLabel = "Show less..."
    } else {
      this.showDataLabel = "Show more..."
    }
  }

  accept(id:number){
    this.certificateDemandService.accept(id).subscribe(
      data =>{
        this.changedStatus.emit(id);
        alert("Certificate approved!");
      }
    )
  }

  decline(id:number){
    this.certificateDemandService.reject(id).subscribe(
      data => {
        this.changedStatus.emit(id);
      }
    )
  }

  getStatusClass() {
    switch (this.certificate.status.toLowerCase()) {
      case 'pending':
        return 'gray';
      case 'rejected':
        return 'red';
      case 'accepted':
        return 'green';
      default:
        return '';
    }
  }
}
