import { Component, Input, Renderer2 } from '@angular/core';
import { CertificateDemandResponse } from 'src/app/core/models/certificate-demand-response.model';

@Component({
  selector: 'app-certificate-demand-model',
  templateUrl: './certificate-demand-model.component.html',
  styleUrls: ['./certificate-demand-model.component.css']
})
export class CertificateDemandModelComponent {
  @Input() certificate: CertificateDemandResponse = {} as CertificateDemandResponse;
  

  constructor(
    private renderer: Renderer2
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
