import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';

@Component({
  selector: 'app-certificate-model',
  templateUrl: './certificate-model.component.html',
  styleUrls: ['./certificate-model.component.css']
})
export class CertificateModelComponent {

  @Input() certificate: CertificateResponse = {} as CertificateResponse;

  @Output() emitter: EventEmitter<CertificateResponse> = new EventEmitter();

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

  // triggers when the user clicks on the certificate (cilcking the 'Show more...' button is no longer treated as clicking on the certificate)
  onClick() {
    this.emitter.emit(this.certificate);
  }

}
