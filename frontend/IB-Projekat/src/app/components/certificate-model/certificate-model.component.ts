import { Component, Input } from '@angular/core';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';

@Component({
  selector: 'app-certificate-model',
  templateUrl: './certificate-model.component.html',
  styleUrls: ['./certificate-model.component.css']
})
export class CertificateModelComponent {

  @Input() certificate: CertificateResponse = {} as CertificateResponse;

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

}
